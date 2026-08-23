package com.example.wordme.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.wordme.MainActivity
import com.example.wordme.R
import com.example.wordme.data.StreakManager
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class WordMeSmallWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        if (action == Intent.ACTION_DATE_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == AppWidgetManager.ACTION_APPWIDGET_UPDATE
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, WordMeSmallWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds.isNotEmpty()) {
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_small)
            val streakManager = StreakManager(context)

            val today = LocalDate.now()
            val lastActiveStr = streakManager.lastActiveDate
            val recoveryDeadline = streakManager.recoveryDeadline
            val recoveryCompletedDate = streakManager.recoveryCompletedDate
            val recoveryFailedDate = streakManager.recoveryFailedDate

            val lastActive = try {
                if (lastActiveStr != null) LocalDate.parse(lastActiveStr) else null
            } catch (e: Exception) {
                null
            }

            val completedToday = lastActive == today

            // Calculate current streak status based on last active date
            var displayedStreak = streakManager.currentStreak
            var isRecoveryRequired = false

            if (lastActive != null) {
                val daysDiff = ChronoUnit.DAYS.between(lastActive, today)
                when {
                    daysDiff <= 1 -> {
                        // Normal active streak
                    }
                    daysDiff == 2L -> {
                        // Missed yesterday, check if already recovered/failed today
                        val todayStr = today.toString()
                        if (recoveryCompletedDate != todayStr && recoveryFailedDate != todayStr) {
                            isRecoveryRequired = true
                        } else if (recoveryFailedDate == todayStr) {
                            // Already failed today, streak is 0
                            displayedStreak = 0
                        }
                    }
                    else -> {
                        // Missed more than 1 day, streak reset
                        displayedStreak = 0
                    }
                }
            } else {
                displayedStreak = 0
            }

            if (displayedStreak == 0) {
                // State 3: No existing streak
                views.setImageViewResource(R.id.widget_icon, R.drawable.ic_sparkle)
                views.setTextViewText(R.id.widget_title, "Start your streak")
                views.setTextViewText(R.id.widget_subtitle, "Learn your first word today")
                
                // Show pill button style
                views.setTextViewText(R.id.widget_button, "Open Word Me! →")
                views.setViewVisibility(R.id.widget_button, View.VISIBLE)
                views.setInt(R.id.widget_button, "setBackgroundResource", R.drawable.bg_pill_button)
                views.setTextColor(R.id.widget_button, context.getColor(R.color.accent_blue))
            } else if (completedToday) {
                // State 2: User completed today's word
                views.setImageViewResource(R.id.widget_icon, R.drawable.ic_fire)
                views.setTextViewText(R.id.widget_title, "$displayedStreak Day Streak")
                views.setTextViewText(R.id.widget_subtitle, "Great job today! 🎉")
                
                // Display label text instead of button: "Come back tomorrow"
                views.setTextViewText(R.id.widget_button, "Come back tomorrow")
                views.setViewVisibility(R.id.widget_button, View.VISIBLE)
                views.setInt(R.id.widget_button, "setBackgroundResource", 0) // Remove pill background
                views.setTextColor(R.id.widget_button, context.getColor(R.color.muted_blue_grey))
            } else {
                // State 1: User has not completed today's word
                views.setImageViewResource(R.id.widget_icon, R.drawable.ic_fire)
                views.setTextViewText(R.id.widget_title, "$displayedStreak Day Streak")

                val motivation = if (isRecoveryRequired || recoveryDeadline > 0L) {
                    "Don't break your streak 🔥"
                } else {
                    "Keep it alive today ✨"
                }
                views.setTextViewText(R.id.widget_subtitle, motivation)

                // Show pill button style
                views.setTextViewText(R.id.widget_button, "Open Word Me! →")
                views.setViewVisibility(R.id.widget_button, View.VISIBLE)
                views.setInt(R.id.widget_button, "setBackgroundResource", R.drawable.bg_pill_button)
                views.setTextColor(R.id.widget_button, context.getColor(R.color.accent_blue))
            }

            // Click Intent to open app directly to Home screen
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("open_home", true)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

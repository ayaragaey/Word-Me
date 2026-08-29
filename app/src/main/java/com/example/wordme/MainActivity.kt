package com.example.wordme

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wordme.components.BottomNavigationBar
import com.example.wordme.components.LevelUpDialog
import com.example.wordme.components.NamePromptDialog
import com.example.wordme.components.StreakCelebrationDialog
import com.example.wordme.components.WordMilestoneCelebrationDialog
import com.example.wordme.navigation.Screen
import com.example.wordme.screens.home.HomeScreen
import com.example.wordme.screens.mywords.MyWordsScreen
import com.example.wordme.screens.milestones.MyMilestonesScreen
import com.example.wordme.screens.recovery.RecoveryScreen
import com.example.wordme.screens.profile.ProfileScreen
import com.example.wordme.ui.WordViewModel
import com.example.wordme.ui.theme.WordMeTheme

import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WordViewModel by viewModels { WordViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let { handleIntent(it) }
        enableEdgeToEdge()
        setContent {
            WordMeTheme {
                val currentUserName = viewModel.userName
                if (!viewModel.onboardingCompleted) {
                    NamePromptDialog(
                        onOnboardingCompleted = { name, goal ->
                            viewModel.completeOnboarding(name, goal)
                        }
                    )
                } else if (viewModel.isRecoveryActive) {
                    RecoveryScreen(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = {
                            BottomNavigationBar(
                                selectedTab = viewModel.selectedTab.id,
                                onTabSelected = { tabId ->
                                    val screen = Screen.values().firstOrNull { it.id == tabId }
                                    if (screen != null) {
                                        viewModel.selectTab(screen)
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        BoxModifier(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    // Show celebration dialog from queue if any
                    viewModel.currentCelebration?.let { celebration ->
                        when (celebration) {
                            is com.example.wordme.ui.Celebration.LevelUp -> {
                                LevelUpDialog(
                                    levelNumber = celebration.levelNumber,
                                    levelName = celebration.levelName,
                                    wordsRequired = celebration.wordsRequired,
                                    userName = currentUserName ?: "Explorer",
                                    onDismiss = { viewModel.dismissCurrentCelebration() }
                                )
                            }
                            is com.example.wordme.ui.Celebration.WordMilestone -> {
                                WordMilestoneCelebrationDialog(
                                    count = celebration.count,
                                    userName = currentUserName ?: "Explorer",
                                    onDismiss = { viewModel.dismissCurrentCelebration() }
                                )
                            }
                            is com.example.wordme.ui.Celebration.StreakMilestone -> {
                                StreakCelebrationDialog(
                                    streak = celebration.days,
                                    userName = currentUserName ?: "Explorer",
                                    onDismiss = { viewModel.dismissCurrentCelebration() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.getBooleanExtra("open_home", false)) {
            viewModel.selectTab(Screen.HOME)
        }
    }

    @Composable
    private fun BoxModifier(modifier: Modifier = Modifier) {
        when (viewModel.selectedTab) {
            Screen.HOME -> HomeScreen(
                viewModel = viewModel,
                modifier = modifier
            )
            Screen.MY_WORDS -> MyWordsScreen(
                learnedWords = viewModel.learnedWords,
                userName = viewModel.userName,
                onNameChanged = { newName -> viewModel.updateUserName(newName) },
                onRehearseWord = { word -> viewModel.startRehearsal(word) },
                onNavigateToHome = { viewModel.selectTab(Screen.HOME) },
                modifier = modifier
            )
            Screen.PROFILE -> ProfileScreen(
                viewModel = viewModel,
                modifier = modifier
            )
            Screen.MY_MILESTONES -> MyMilestonesScreen(
                viewModel = viewModel,
                modifier = modifier
            )
        }
    }
}
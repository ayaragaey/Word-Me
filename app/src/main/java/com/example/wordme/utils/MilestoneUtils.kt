package com.example.wordme.utils

enum class MilestoneStatus {
    UNLOCKED,
    IN_PROGRESS,
    LOCKED // Up next
}

data class MilestoneItem(
    val id: String,
    val title: String,
    val description: String,
    val currentValue: Int,
    val targetValue: Int,
    val status: MilestoneStatus,
    val category: String // "word" or "streak"
)

object MilestoneUtils {

    fun getWordMilestones(wordsLearned: Int): List<MilestoneItem> {
        val target = when {
            wordsLearned < 1 -> 1
            wordsLearned in 1..9 -> 10
            wordsLearned in 10..24 -> 25
            wordsLearned in 25..49 -> 50
            else -> {
                if (wordsLearned % 50 == 0) wordsLearned + 50 else ((wordsLearned / 50) + 1) * 50
            }
        }

        val previous = when (target) {
            1 -> null
            10 -> 1
            25 -> 10
            50 -> 25
            100 -> 50
            else -> target - 50
        }

        val next = when (target) {
            1 -> 10
            10 -> 25
            25 -> 50
            50 -> 100
            else -> target + 50
        }

        val list = mutableListOf<MilestoneItem>()

        // 1. Recently Unlocked
        if (previous != null) {
            list.add(
                MilestoneItem(
                    id = "word_$previous",
                    title = if (previous == 1) "First Word" else "$previous Words Learned",
                    description = if (previous == 1) "Learn your first word." else "Learn $previous words.",
                    currentValue = previous,
                    targetValue = previous,
                    status = MilestoneStatus.UNLOCKED,
                    category = "word"
                )
            )
        }

        // 2. In Progress
        list.add(
            MilestoneItem(
                id = "word_$target",
                title = if (target == 1) "First Word" else "$target Words Learned",
                description = if (target == 1) "Learn your first word." else "Learn $target words.",
                currentValue = wordsLearned,
                targetValue = target,
                status = MilestoneStatus.IN_PROGRESS,
                category = "word"
            )
        )

        // 3. Up Next (Locked)
        if (next <= 500000) {
            list.add(
                MilestoneItem(
                    id = "word_$next",
                    title = "$next Words Learned",
                    description = "Learn $next words.",
                    currentValue = wordsLearned,
                    targetValue = next,
                    status = MilestoneStatus.LOCKED,
                    category = "word"
                )
            )
        }

        return list
    }

    fun getStreakMilestones(streakCount: Int): List<MilestoneItem> {
        val target = when {
            streakCount < 3 -> 3
            streakCount in 3..6 -> 7
            else -> {
                if (streakCount % 7 == 0) streakCount + 7 else ((streakCount / 7) + 1) * 7
            }
        }

        val previous = when (target) {
            3 -> null
            7 -> 3
            14 -> 7
            else -> target - 7
        }

        val next = when (target) {
            3 -> 7
            7 -> 14
            else -> target + 7
        }

        val list = mutableListOf<MilestoneItem>()

        // 1. Recently Unlocked
        if (previous != null) {
            list.add(
                MilestoneItem(
                    id = "streak_$previous",
                    title = "$previous-Day Streak",
                    description = "Maintain a $previous-day streak.",
                    currentValue = previous,
                    targetValue = previous,
                    status = MilestoneStatus.UNLOCKED,
                    category = "streak"
                )
            )
        }

        // 2. In Progress
        list.add(
            MilestoneItem(
                id = "streak_$target",
                title = "$target-Day Streak",
                description = "Maintain a $target-day streak.",
                currentValue = streakCount,
                targetValue = target,
                status = MilestoneStatus.IN_PROGRESS,
                category = "streak"
            )
        )

        // 3. Up Next (Locked)
        list.add(
            MilestoneItem(
                id = "streak_$next",
                title = "$next-Day Streak",
                description = "Maintain a $next-day streak.",
                currentValue = streakCount,
                targetValue = next,
                status = MilestoneStatus.LOCKED,
                category = "streak"
            )
        )

        return list
    }
}

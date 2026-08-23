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
        val target = ((wordsLearned / 50) + 1) * 50
        val cappedTarget = if (target > 500000) 500000 else target

        val previous = if (cappedTarget - 50 >= 50) cappedTarget - 50 else null
        val next = if (cappedTarget + 50 <= 500000) cappedTarget + 50 else null

        val list = mutableListOf<MilestoneItem>()

        // 1. Recently Unlocked
        if (previous != null) {
            list.add(
                MilestoneItem(
                    id = "word_$previous",
                    title = "$previous Words Learned",
                    description = "Learn $previous words.",
                    currentValue = previous,
                    targetValue = previous,
                    status = MilestoneStatus.UNLOCKED,
                    category = "word"
                )
            )
        }

        // 2. In Progress / Completed
        if (wordsLearned < 500000) {
            list.add(
                MilestoneItem(
                    id = "word_$cappedTarget",
                    title = "$cappedTarget Words Learned",
                    description = "Learn $cappedTarget words.",
                    currentValue = wordsLearned,
                    targetValue = cappedTarget,
                    status = MilestoneStatus.IN_PROGRESS,
                    category = "word"
                )
            )
        } else {
            list.add(
                MilestoneItem(
                    id = "word_500000",
                    title = "500,000 Words Learned",
                    description = "Learn 500,000 words.",
                    currentValue = 500000,
                    targetValue = 500000,
                    status = MilestoneStatus.UNLOCKED,
                    category = "word"
                )
            )
        }

        // 3. Up Next (Locked)
        if (next != null && wordsLearned < 500000) {
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

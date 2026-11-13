package com.example.model

data class CompetencyArea(
    val id: Int,
    val name: String,
    val practice: String
)

data class SjtItem(
    val id: Int,
    val title: String,
    val areaIndex: Int,
    val scene: String,
    /**
     * Az opciók tömbjében az index kétszerese az előzetes pontérték (0, 2, 4, 6).
     */
    val options: List<String>,
    val why: String,
    val worstKeyRank: Int = 0
)

data class ItemAnswer(
    val bestRank: Int? = null,
    val worstRank: Int? = null
) {
    val isComplete: Boolean
        get() = bestRank != null && worstRank != null && bestRank != worstRank
}

data class CompetencyScore(
    val area: CompetencyArea,
    val score: Int,
    val maxScore: Int
) {
    val ratio: Float
        get() = if (maxScore > 0) score.toFloat() / maxScore.toFloat() else 0f
}

data class ItemEvaluation(
    val itemIndex: Int,
    val item: SjtItem,
    val totalPoints: Int,
    val bestRank: Int,
    val bestPoints: Int,
    val worstRank: Int,
    val worstPoints: Int,
    val displayOrder: List<Int>
) {
    fun letterForRank(rank: Int): Char {
        val pos = displayOrder.indexOf(rank)
        return if (pos in 0..3) 'A' + pos else '?'
    }

    fun describeRank(rank: Int): String = "${letterForRank(rank)}. ${item.options[rank]}"
}

data class SjtReport(
    val participantName: String,
    val formattedDate: String,
    val totalScore: Int,
    val maxTotalScore: Int,
    val competencyProfile: List<CompetencyScore>,
    val needsSafetyConsultation: Boolean,
    val priorities: List<CompetencyScore>,
    val itemEvaluations: List<ItemEvaluation>
)

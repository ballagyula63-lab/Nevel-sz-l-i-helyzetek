package com.example

import com.example.data.SjtRepository
import com.example.model.ItemAnswer
import com.example.ui.ScrollTarget
import com.example.ui.SjtViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class SjtScoringAndStateTest {

    @Test
    fun perfectScore_returns90AndNoPrioritiesOrSafetyAlert() {
        val answers = List(SjtRepository.items.size) { ItemAnswer(bestRank = 3, worstRank = 0) }
        val orders = List(SjtRepository.items.size) { listOf(0, 1, 2, 3) }

        val report = SjtRepository.evaluateTest(
            participantName = "  Kovács Anna  ",
            answers = answers,
            orders = orders,
            formattedDate = "2026. november 13."
        )

        assertEquals("Kovács Anna", report.participantName)
        assertEquals(90, report.totalScore)
        assertEquals(90, report.maxTotalScore)
        assertFalse(report.needsSafetyConsultation)
        assertTrue(report.priorities.isEmpty())
        assertEquals(27, report.competencyProfile[0].score)
        assertEquals(18, report.competencyProfile[1].score)
        assertEquals(27, report.competencyProfile[2].score)
        assertEquals(18, report.competencyProfile[3].score)
    }

    @Test
    fun partialScore_triggersSafetyConsultationAndRanksTwoLowestPriorities() {
        val answers = MutableList(SjtRepository.items.size) { ItemAnswer(bestRank = 3, worstRank = 0) }
        // Lower item 7 (area 3, safety) and item 1 (area 1)
        answers[7] = ItemAnswer(bestRank = 1, worstRank = 2) // 2 + 0 = 2/9 -> area 3 = 11/18
        answers[1] = ItemAnswer(bestRank = 0, worstRank = 3) // 0 + 0 = 0/9 -> area 1 = 9/18 (0.5)

        val orders = List(SjtRepository.items.size) { listOf(3, 2, 1, 0) }
        val report = SjtRepository.evaluateTest(
            participantName = "Teszt Kitöltő",
            answers = answers,
            orders = orders,
            formattedDate = "2026. november 13."
        )

        assertTrue(report.needsSafetyConsultation)
        assertEquals(2, report.priorities.size)
        assertEquals(1, report.priorities[0].area.id)
        assertEquals(3, report.priorities[1].area.id)
        // Check display letter mapping for reversed order [3, 2, 1, 0] -> rank 3 is 'A', rank 0 is 'D'
        assertEquals('A', report.itemEvaluations[0].letterForRank(3))
        assertEquals('D', report.itemEvaluations[0].letterForRank(0))
    }

    @Test
    fun shuffledOrders_areValidPermutations() {
        val orders = SjtRepository.generateShuffledOrders(random = Random(42))
        assertEquals(10, orders.size)
        orders.forEach { order ->
            assertEquals(setOf(0, 1, 2, 3), order.toSet())
        }
    }

    @Test
    fun viewModel_enforcesMutualExclusionValidationAndRestart() {
        val vm = SjtViewModel()

        // Mutual exclusion: selecting rank 2 as best blocks selecting rank 2 as worst
        vm.onSelectBest(0, 2)
        vm.onSelectWorst(0, 2)
        assertEquals(2, vm.uiState.value.answers[0].bestRank)
        assertNull(vm.uiState.value.answers[0].worstRank)

        // Submitting without participant name flags nameError
        vm.onSubmit()
        assertEquals("Adja meg a kitöltő nevét.", vm.uiState.value.nameError)
        assertEquals(ScrollTarget.NameField, vm.uiState.value.scrollTarget)

        // Submitting with name but incomplete scenarios flags scenario 0
        vm.onParticipantNameChanged("Nagy Éva")
        vm.onSubmit()
        assertEquals(
            "A(z) 1. helyzetben jelölje meg a legmegfelelőbb és a legkevésbé megfelelő reakciót.",
            vm.uiState.value.formError
        )
        assertEquals(ScrollTarget.Scenario(0), vm.uiState.value.scrollTarget)

        // Complete all scenarios and submit
        for (i in 0 until 10) {
            vm.onSelectBest(i, 3)
            vm.onSelectWorst(i, 0)
        }
        vm.onSubmit()
        assertNotNull(vm.uiState.value.report)
        assertEquals(90, vm.uiState.value.report?.totalScore)

        // Edit preserves answers and clears report
        vm.onEditAnswers()
        assertNull(vm.uiState.value.report)
        assertEquals(10, vm.uiState.value.completedCount)

        // Confirm restart clears name and answers
        vm.onConfirmRestart()
        assertEquals("", vm.uiState.value.participantName)
        assertEquals(0, vm.uiState.value.completedCount)
        assertNull(vm.uiState.value.report)
    }
}

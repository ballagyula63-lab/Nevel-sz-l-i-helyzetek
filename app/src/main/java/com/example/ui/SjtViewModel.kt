package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.SjtRepository
import com.example.model.ItemAnswer
import com.example.model.SjtItem
import com.example.model.SjtReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface ScrollTarget {
    data object Top : ScrollTarget
    data object NameField : ScrollTarget
    data class Scenario(val index: Int) : ScrollTarget
}

data class SjtUiState(
    val participantName: String = "",
    val nameError: String? = null,
    val items: List<SjtItem> = SjtRepository.items,
    val orders: List<List<Int>> = SjtRepository.generateShuffledOrders(),
    val answers: List<ItemAnswer> = List(SjtRepository.items.size) { ItemAnswer() },
    val formError: String? = null,
    val report: SjtReport? = null,
    val isPrivacyExpanded: Boolean = false,
    val showRestartDialog: Boolean = false,
    val scrollTarget: ScrollTarget? = null
) {
    val completedCount: Int
        get() = answers.count { it.isComplete }

    val totalCount: Int
        get() = items.size

    val isShowingReport: Boolean
        get() = report != null
}

class SjtViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SjtUiState())
    val uiState: StateFlow<SjtUiState> = _uiState.asStateFlow()

    fun onParticipantNameChanged(value: String) {
        _uiState.update { state ->
            state.copy(
                participantName = value,
                nameError = null
            )
        }
    }

    fun onTogglePrivacy() {
        _uiState.update { state ->
            state.copy(isPrivacyExpanded = !state.isPrivacyExpanded)
        }
    }

    fun onSelectBest(itemIndex: Int, rank: Int) {
        _uiState.update { state ->
            val current = state.answers[itemIndex]
            if (current.worstRank == rank) return@update state
            val updated = state.answers.toMutableList().apply {
                this[itemIndex] = current.copy(bestRank = rank)
            }
            state.copy(
                answers = updated,
                formError = null
            )
        }
    }

    fun onSelectWorst(itemIndex: Int, rank: Int) {
        _uiState.update { state ->
            val current = state.answers[itemIndex]
            if (current.bestRank == rank) return@update state
            val updated = state.answers.toMutableList().apply {
                this[itemIndex] = current.copy(worstRank = rank)
            }
            state.copy(
                answers = updated,
                formError = null
            )
        }
    }

    fun onJumpToScenario(index: Int) {
        _uiState.update { it.copy(scrollTarget = ScrollTarget.Scenario(index)) }
    }

    fun onSubmit() {
        val current = _uiState.value
        if (current.participantName.trim().isEmpty()) {
            _uiState.update {
                it.copy(
                    nameError = "Adja meg a kitöltő nevét.",
                    scrollTarget = ScrollTarget.NameField
                )
            }
            return
        }

        val missingIndex = current.answers.indexOfFirst { !it.isComplete }
        if (missingIndex != -1) {
            _uiState.update {
                it.copy(
                    formError = "A(z) ${missingIndex + 1}. helyzetben jelölje meg a legmegfelelőbb és a legkevésbé megfelelő reakciót.",
                    scrollTarget = ScrollTarget.Scenario(missingIndex)
                )
            }
            return
        }

        val report = SjtRepository.evaluateTest(
            participantName = current.participantName,
            answers = current.answers,
            orders = current.orders,
            items = current.items
        )

        _uiState.update {
            it.copy(
                formError = null,
                nameError = null,
                report = report,
                scrollTarget = ScrollTarget.Top
            )
        }
    }

    fun onEditAnswers() {
        _uiState.update {
            it.copy(
                report = null,
                scrollTarget = ScrollTarget.Scenario(0)
            )
        }
    }

    fun onRequestRestart() {
        _uiState.update { it.copy(showRestartDialog = true) }
    }

    fun onDismissRestart() {
        _uiState.update { it.copy(showRestartDialog = false) }
    }

    fun onConfirmRestart() {
        _uiState.update {
            SjtUiState(
                items = SjtRepository.items,
                orders = SjtRepository.generateShuffledOrders(),
                answers = List(SjtRepository.items.size) { ItemAnswer() },
                scrollTarget = ScrollTarget.Top
            )
        }
    }

    fun onScrollTargetHandled() {
        _uiState.update { it.copy(scrollTarget = null) }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.ScrollTarget
import com.example.ui.SjtUiState
import com.example.ui.components.IntroGuideCard
import com.example.ui.components.ParticipantNameCard
import com.example.ui.components.ScenarioItemCard
import com.example.ui.components.SjtHeaderBanner
import com.example.ui.components.StickyProgressHeader

@Composable
fun SjtTestScreen(
    uiState: SjtUiState,
    onParticipantNameChanged: (String) -> Unit,
    onTogglePrivacy: () -> Unit,
    onSelectBest: (Int, Int) -> Unit,
    onSelectWorst: (Int, Int) -> Unit,
    onJumpToScenario: (Int) -> Unit,
    onSubmit: () -> Unit,
    onScrollTargetHandled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // List item indices:
    // 0: Header Banner
    // 1: Intro Guide
    // 2: Participant Name Card
    // 3..(3 + items.size - 1): Scenarios
    // last: Error + Submit Button
    LaunchedEffect(uiState.scrollTarget) {
        val target = uiState.scrollTarget ?: return@LaunchedEffect
        val listIndex = when (target) {
            ScrollTarget.Top -> 0
            ScrollTarget.NameField -> 2
            is ScrollTarget.Scenario -> 3 + target.index
        }
        listState.animateScrollToItem(listIndex)
        onScrollTargetHandled()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StickyProgressHeader(
            completedCount = uiState.completedCount,
            totalCount = uiState.totalCount,
            answers = uiState.answers,
            onJumpToScenario = onJumpToScenario,
            modifier = Modifier.widthIn(max = 760.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(key = "header_banner") {
                    SjtHeaderBanner()
                }

                item(key = "intro_guide") {
                    IntroGuideCard(
                        isPrivacyExpanded = uiState.isPrivacyExpanded,
                        onTogglePrivacy = onTogglePrivacy
                    )
                }

                item(key = "participant_name") {
                    ParticipantNameCard(
                        name = uiState.participantName,
                        nameError = uiState.nameError,
                        onNameChange = onParticipantNameChanged
                    )
                }

                itemsIndexed(
                    items = uiState.items,
                    key = { _, item -> "scenario_${item.id}" }
                ) { index, item ->
                    ScenarioItemCard(
                        itemIndex = index,
                        item = item,
                        displayOrder = uiState.orders[index],
                        answer = uiState.answers[index],
                        onSelectBest = { rank -> onSelectBest(index, rank) },
                        onSelectWorst = { rank -> onSelectWorst(index, rank) }
                    )
                }

                item(key = "submit_footer") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.formError != null) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.errorContainer
                            ) {
                                Text(
                                    text = uiState.formError,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                        .testTag("form_error_text")
                                )
                            }
                        }

                        Button(
                            onClick = onSubmit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 52.dp)
                                .testTag("submit_test_button"),
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Értékelés megtekintése",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

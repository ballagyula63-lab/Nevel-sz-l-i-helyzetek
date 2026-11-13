package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.ItemAnswer
import com.example.model.SjtItem
import com.example.ui.theme.LocalSjtColors

@Composable
fun SjtHeaderBanner(modifier: Modifier = Modifier) {
    val sjtColors = LocalSjtColors.current
    val baseColor = sjtColors.headerForest
    val accentColor = sjtColors.headerForestAccent

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = baseColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(baseColor, accentColor),
                            start = Offset.Zero,
                            end = Offset(size.width, size.height)
                        )
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.06f),
                        radius = size.width * 0.45f,
                        center = Offset(size.width * 0.92f, size.height * 0.15f)
                    )
                }
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btesz_logo),
                        contentDescription = "Baptista Tevékeny Szeretet Misszió",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                            .width(126.dp)
                            .height(68.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "NEVELŐSZÜLŐI HELYZETMEGÍTÉLÉSI TESZT",
                    style = MaterialTheme.typography.labelMedium,
                    color = sjtColors.onHeaderForest.copy(alpha = 0.88f)
                )
                Text(
                    text = "Hazudozás és szabályszegés",
                    style = MaterialTheme.typography.headlineLarge,
                    color = sjtColors.onHeaderForest
                )
                Text(
                    text = "10 helyzet nevelőszülői önreflexióhoz és képzéshez",
                    style = MaterialTheme.typography.bodyLarge,
                    color = sjtColors.onHeaderForest.copy(alpha = 0.92f)
                )
            }
        }
    }
}

@Composable
fun AmberNoticeCallout(
    boldPrefix: String,
    bodyText: String,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .background(sjtColors.noticeBackground)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(sjtColors.noticeBorder)
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Rounded.WarningAmber,
                contentDescription = null,
                tint = sjtColors.noticeBorder,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(20.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(boldPrefix)
                    }
                    if (bodyText.isNotEmpty()) {
                        append(" ")
                        append(bodyText)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = sjtColors.noticeText
            )
        }
    }
}

@Composable
fun IntroGuideCard(
    isPrivacyExpanded: Boolean,
    onTogglePrivacy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Kitöltési útmutató",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = buildAnnotatedString {
                    append("Képzelje el, hogy Ön kerül a leírt helyzetbe. Gondolja végig, mit tenne. A négy reakció közül jelölje meg, melyiket tartaná a ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("legmegfelelőbbnek")
                    }
                    append(", és melyiket a ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("legkevésbé megfelelőnek")
                    }
                    append(". Két különböző reakciót jelöljön meg.")
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            AmberNoticeCallout(
                boldPrefix = "Ez a teszt nem validált. Önálló kiválasztási vagy alkalmassági döntésre nem alkalmas.",
                bodyText = "A pontozási kulcs előzetes szakmai javaslat, külső szakértői felülvizsgálat és kipróbálás nélkül."
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(onClick = onTogglePrivacy)
                    .testTag("privacy_toggle"),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, sjtColors.cardBorder.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Adatvédelmi tájékoztató",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (isPrivacyExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = if (isPrivacyExpanded) "Bezárás" else "Megnyitás",
                            tint = sjtColors.mutedText
                        )
                    }

                    AnimatedVisibility(visible = isPrivacyExpanded) {
                        Column(
                            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "A teszt internetkapcsolat nélkül, helyben fut. A kitöltő neve azonosítóként szerepel az értékelőlapon. A teszt a nevet és a válaszokat nem küldi el és nem menti el. Ezek az aktuális megnyitás idejére az alkalmazás memóriájában maradnak. Bezáráskor vagy új kitöltés indításakor törlődnek.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = sjtColors.mutedText
                            )
                            Text(
                                text = "A nyomtatott vagy PDF-be mentett összesítő külön példány, amelyet Ön kezel.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = sjtColors.mutedText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParticipantNameCard(
    name: String,
    nameError: String?,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (nameError != null) MaterialTheme.colorScheme.error else sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Kitöltő neve (kitöltési azonosító)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("participant_name_input"),
                singleLine = true,
                isError = nameError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Badge,
                        contentDescription = null,
                        tint = if (nameError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                },
                placeholder = {
                    Text(
                        text = "Adja meg a nevét vagy azonosítóját",
                        color = sjtColors.mutedText.copy(alpha = 0.7f)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = sjtColors.mutedText,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                shape = MaterialTheme.shapes.small
            )
            if (nameError != null) {
                Text(
                    text = nameError,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("name_error_text")
                )
            }
        }
    }
}

@Composable
fun StickyProgressHeader(
    completedCount: Int,
    totalCount: Int,
    answers: List<ItemAnswer>,
    onJumpToScenario: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    val targetProgress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(),
        label = "sjt_progress"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kitöltött helyzetek $completedCount/$totalCount",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.testTag("progress_label")
                )
                Text(
                    text = "${(targetProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = sjtColors.progressTrack,
                strokeCap = StrokeCap.Round
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                answers.forEachIndexed { index, ans ->
                    val complete = ans.isComplete
                    val partial = !complete && (ans.bestRank != null || ans.worstRank != null)
                    val bg by animateColorAsState(
                        targetValue = when {
                            complete -> MaterialTheme.colorScheme.primary
                            partial -> sjtColors.noticeBackground
                            else -> MaterialTheme.colorScheme.surface
                        },
                        label = "chip_bg_$index"
                    )
                    val textColor = when {
                        complete -> MaterialTheme.colorScheme.onPrimary
                        partial -> sjtColors.noticeText
                        else -> sjtColors.mutedText
                    }
                    val border = when {
                        complete -> MaterialTheme.colorScheme.primary
                        partial -> sjtColors.noticeBorder
                        else -> sjtColors.cardBorder
                    }
                    Surface(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onJumpToScenario(index) }
                            .semantics {
                                contentDescription = "${index + 1}. helyzet, ${
                                    when {
                                        complete -> "kitöltve"
                                        partial -> "részben kitöltve"
                                        else -> "kitöltetlen"
                                    }
                                }"
                            }
                            .testTag("jump_chip_$index"),
                        shape = RoundedCornerShape(10.dp),
                        color = bg,
                        border = BorderStroke(1.dp, border)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScenarioItemCard(
    itemIndex: Int,
    item: SjtItem,
    displayOrder: List<Int>,
    answer: ItemAnswer,
    onSelectBest: (Int) -> Unit,
    onSelectWorst: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    val isComplete = answer.isComplete

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("scenario_card_$itemIndex"),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = if (isComplete) 1.5.dp else 1.dp,
            color = if (isComplete) MaterialTheme.colorScheme.primary.copy(alpha = 0.65f) else sjtColors.cardBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${itemIndex + 1}. ${item.title}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val statusLabel = when {
                    isComplete -> "Kész"
                    answer.bestRank != null || answer.worstRank != null -> "1/2"
                    else -> "0/2"
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = if (isComplete) sjtColors.bestSelectedBackground else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (isComplete) sjtColors.bestSelectedBorder.copy(alpha = 0.45f) else sjtColors.cardBorder
                    )
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isComplete) sjtColors.bestSelectedContent else sjtColors.mutedText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = item.scene,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, sjtColors.cardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Ön mit tenne ebben a helyzetben?",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    displayOrder.forEachIndexed { position, rank ->
                        val letter = 'A' + position
                        val optionText = item.options[rank]
                        val isBestSelected = answer.bestRank == rank
                        val isWorstSelected = answer.worstRank == rank
                        val isBestDisabled = isWorstSelected
                        val isWorstDisabled = isBestSelected

                        val rowBg = when {
                            isBestSelected -> sjtColors.bestSelectedBackground.copy(alpha = 0.5f)
                            isWorstSelected -> sjtColors.worstSelectedBackground.copy(alpha = 0.5f)
                            else -> Color.Transparent
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(rowBg)
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("$letter. ")
                                    }
                                    append(optionText)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RatingChoicePill(
                                    label = "Legmegfelelőbb",
                                    isSelected = isBestSelected,
                                    isDisabled = isBestDisabled,
                                    isBestKind = true,
                                    accessibilityLabel = "$letter. Legmegfelelőbb",
                                    onClick = { onSelectBest(rank) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("option_best_${itemIndex}_$position")
                                )
                                RatingChoicePill(
                                    label = "Legkevésbé megfelelő",
                                    isSelected = isWorstSelected,
                                    isDisabled = isWorstDisabled,
                                    isBestKind = false,
                                    accessibilityLabel = "$letter. Legkevésbé megfelelő",
                                    onClick = { onSelectWorst(rank) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("option_worst_${itemIndex}_$position")
                                )
                            }
                        }

                        if (position < displayOrder.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = sjtColors.cardBorder.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingChoicePill(
    label: String,
    isSelected: Boolean,
    isDisabled: Boolean,
    isBestKind: Boolean,
    accessibilityLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    val selectedBg = if (isBestKind) sjtColors.bestSelectedBackground else sjtColors.worstSelectedBackground
    val selectedBorder = if (isBestKind) sjtColors.bestSelectedBorder else sjtColors.worstSelectedBorder
    val selectedContent = if (isBestKind) sjtColors.bestSelectedContent else sjtColors.worstSelectedContent

    val containerColor = if (isSelected) selectedBg else MaterialTheme.colorScheme.surface
    val borderColor = if (isSelected) selectedBorder else sjtColors.cardBorder
    val contentColor = if (isSelected) selectedContent else sjtColors.mutedText

    val icon = when {
        isBestKind && isSelected -> Icons.Filled.CheckCircle
        isBestKind -> Icons.Outlined.CheckCircle
        isSelected -> Icons.Filled.DoNotDisturbOn
        else -> Icons.Outlined.DoNotDisturbOn
    }

    Surface(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .alpha(if (isDisabled) 0.45f else 1f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                enabled = !isDisabled,
                role = Role.RadioButton,
                onClick = onClick
            )
            .semantics {
                selected = isSelected
                contentDescription = accessibilityLabel
            },
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

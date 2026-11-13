package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CompetencyScore
import com.example.model.ItemEvaluation
import com.example.model.SjtReport
import com.example.ui.components.AmberNoticeCallout
import com.example.ui.theme.LocalSjtColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SjtResultsScreen(
    report: SjtReport,
    showRestartDialog: Boolean,
    onPrintReport: () -> Unit,
    onEditAnswers: () -> Unit,
    onRequestRestart: () -> Unit,
    onDismissRestart: () -> Unit,
    onConfirmRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    val listState = rememberLazyListState()

    BackHandler(onBack = onEditAnswers)

    LaunchedEffect(report) {
        listState.scrollToItem(0)
    }

    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = onDismissRestart,
            title = {
                Text(
                    text = "Új kitöltés indítása",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "Törli a jelenlegi válaszokat, és új kitöltést indít?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirmRestart,
                    modifier = Modifier.testTag("confirm_restart_button")
                ) {
                    Text("Új kitöltés")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRestart,
                    modifier = Modifier.testTag("cancel_restart_button")
                ) {
                    Text("Mégse")
                }
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
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
            item(key = "report_summary") {
                ReportSummaryCard(report = report)
            }

            item(key = "competency_profile") {
                CompetencyProfileCard(profile = report.competencyProfile)
            }

            item(key = "development_feedback") {
                DevelopmentFeedbackCard(report = report)
            }

            item(key = "review_header") {
                Text(
                    text = "Visszajelzés helyzetenként",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(
                items = report.itemEvaluations,
                key = { eval -> "eval_${eval.item.id}" }
            ) { eval ->
                ItemEvaluationCard(eval = eval)
            }

            item(key = "scoring_method") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, sjtColors.cardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pontozás",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "A legmegfelelőbbnek jelölt reakció 0, 2, 4 vagy 6 pontot ér a szakmai kulcs szerint. A kulcs szerinti legkevésbé megfelelő reakció felismerése további 3 pontot ér, minden más jelölés ebben a kategóriában 0 pontot. Helyzetenként legfeljebb 9, összesen 90 pont szerezhető.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "A tételek és a kulcs saját fejlesztésűek. A szemlélethez felhasznált háttéranyagok az NCTSN nevelőszülői tananyaga és az NSPCC útmutatója a bántalmazásról szóló közlések fogadásához.",
                            style = MaterialTheme.typography.bodySmall,
                            color = sjtColors.mutedText
                        )
                    }
                }
            }

            item(key = "report_actions") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPrintReport,
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("print_button"),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Print,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nyomtatás / mentés PDF-be")
                    }

                    OutlinedButton(
                        onClick = onEditAnswers,
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("edit_button"),
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Vissza a válaszokhoz")
                    }

                    OutlinedButton(
                        onClick = onRequestRestart,
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("restart_button"),
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Új kitöltés")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportSummaryCard(report: SjtReport) {
    val sjtColors = LocalSjtColors.current
    val ratio = if (report.maxTotalScore > 0) {
        report.totalScore.toFloat() / report.maxTotalScore.toFloat()
    } else 0f
    val animatedRatio by animateFloatAsState(
        targetValue = ratio,
        animationSpec = spring(),
        label = "total_score_ratio"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = BorderStroke(1.dp, sjtColors.cardBorder.copy(alpha = 0.5f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btesz_logo),
                    contentDescription = "Baptista Tevékeny Szeretet Misszió",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .width(120.dp)
                        .height(64.dp)
                )
            }

            Text(
                text = "Egyéni értékelőlap",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Nevelőszülői SJT – Hazudozás és szabályszegés",
                style = MaterialTheme.typography.bodyMedium,
                color = sjtColors.mutedText
            )

            HorizontalDivider(color = sjtColors.cardBorder)

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Kitöltési azonosító")
                    }
                    append(" · ")
                    append(report.participantName)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("result_participant_name")
            )
            Text(
                text = "Kitöltés dátuma: ${report.formattedDate}",
                style = MaterialTheme.typography.bodySmall,
                color = sjtColors.mutedText
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "${report.totalScore} / ${report.maxTotalScore} pont",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("result_total_score")
                )
                LinearProgressIndicator(
                    progress = { animatedRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = sjtColors.progressTrack,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            AmberNoticeCallout(
                boldPrefix = "Nem validált teszt. Önálló kiválasztási vagy alkalmassági döntésre nem alkalmas.",
                bodyText = "Az eredmény azt mutatja, mennyire egyeznek a válaszok az előzetes szakmai kulccsal. Nincs megfelelési küszöb vagy összehasonlítási norma."
            )
        }
    }
}

@Composable
private fun CompetencyProfileCard(profile: List<CompetencyScore>) {
    val sjtColors = LocalSjtColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Kompetenciaprofil",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "A részpontszámok területenként 2–3 helyzetből állnak össze.",
                style = MaterialTheme.typography.bodySmall,
                color = sjtColors.mutedText
            )

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val useTwoColumns = maxWidth >= 520.dp
                if (useTwoColumns) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        profile.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { area ->
                                    CompetencyItemBox(
                                        area = area,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        profile.forEach { area ->
                            CompetencyItemBox(
                                area = area,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompetencyItemBox(
    area: CompetencyScore,
    modifier: Modifier = Modifier
) {
    val sjtColors = LocalSjtColors.current
    val animatedRatio by animateFloatAsState(
        targetValue = area.ratio,
        animationSpec = spring(),
        label = "competency_meter_${area.area.id}"
    )

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = area.area.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${area.score} / ${area.maxScore} pont",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
            LinearProgressIndicator(
                progress = { animatedRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = sjtColors.progressTrack,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun DevelopmentFeedbackCard(report: SjtReport) {
    val sjtColors = LocalSjtColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Fejlesztési visszajelzés",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (report.needsSafetyConsultation) {
                AmberNoticeCallout(
                    boldPrefix = "Beszélje át a 8. és a 10. helyzetet szakmai konzultáción.",
                    bodyText = "Gyakorolja a közvetlen veszély megszakítását és a bántalmazásról szóló közlésre adott első lépéseket."
                )
            }

            if (report.priorities.isNotEmpty()) {
                report.priorities.forEach { priority ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${priority.area.name}. ")
                                }
                                append(priority.area.practice)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                Text(
                    text = "Minden választása egyezett a kulccsal. Képzésen gyakorolja, hogyan mondaná el a gyermeknek a választott reakció első mondatát, és mi lenne a következő lépése.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ItemEvaluationCard(eval: ItemEvaluation) {
    val sjtColors = LocalSjtColors.current
    val isFullScore = eval.totalPoints == 9

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, sjtColors.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${eval.itemIndex + 1}. ${eval.item.title}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = if (isFullScore) sjtColors.bestSelectedBackground else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (isFullScore) sjtColors.bestSelectedBorder.copy(alpha = 0.5f) else sjtColors.cardBorder
                    )
                ) {
                    Text(
                        text = "${eval.totalPoints}/9 pont",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isFullScore) sjtColors.bestSelectedContent else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)) {
                        append("Legmegfelelőbbnek jelölte (${eval.bestPoints}/6 pont): ")
                    }
                    append(eval.describeRank(eval.bestRank))
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = sjtColors.worstSelectedContent)) {
                        append("Legkevésbé megfelelőnek jelölte (${eval.worstPoints}/3 pont): ")
                    }
                    append(eval.describeRank(eval.worstRank))
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (eval.worstRank != eval.item.worstKeyRank) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("A kulcs szerinti legkevésbé megfelelő: ")
                        }
                        append(eval.describeRank(eval.item.worstKeyRank))
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (eval.bestRank != 3) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("A kulcs szerinti legmegfelelőbb: ")
                        }
                        append(eval.describeRank(3))
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 2.dp),
                color = sjtColors.cardBorder.copy(alpha = 0.7f)
            )

            Text(
                text = eval.item.why,
                style = MaterialTheme.typography.bodyMedium,
                color = sjtColors.mutedText
            )
        }
    }
}

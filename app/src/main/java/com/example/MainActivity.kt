package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.print.SjtReportPrinter
import com.example.ui.SjtViewModel
import com.example.ui.screens.SjtResultsScreen
import com.example.ui.screens.SjtTestScreen
import com.example.ui.theme.NeveloszuloSjtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeveloszuloSjtTheme {
                val viewModel: SjtViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val context = LocalContext.current

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Crossfade(
                        targetState = uiState.report,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        label = "sjt_screen_transition"
                    ) { report ->
                        if (report == null) {
                            SjtTestScreen(
                                uiState = uiState,
                                onParticipantNameChanged = viewModel::onParticipantNameChanged,
                                onTogglePrivacy = viewModel::onTogglePrivacy,
                                onSelectBest = viewModel::onSelectBest,
                                onSelectWorst = viewModel::onSelectWorst,
                                onJumpToScenario = viewModel::onJumpToScenario,
                                onSubmit = viewModel::onSubmit,
                                onScrollTargetHandled = viewModel::onScrollTargetHandled
                            )
                        } else {
                            SjtResultsScreen(
                                report = report,
                                showRestartDialog = uiState.showRestartDialog,
                                onPrintReport = { SjtReportPrinter.printReport(context, report) },
                                onEditAnswers = viewModel::onEditAnswers,
                                onRequestRestart = viewModel::onRequestRestart,
                                onDismissRestart = viewModel::onDismissRestart,
                                onConfirmRestart = viewModel::onConfirmRestart
                            )
                        }
                    }
                }
            }
        }
    }
}

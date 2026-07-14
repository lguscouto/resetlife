package br.com.resetlife.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.resetlife.domain.onboarding.LifeArea
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    selectedArea: LifeArea?,
    onAreaSelected: (LifeArea) -> Unit,
    onNext: () -> Unit,
    step: Int,
    selectedMinutes: Int?,
    onMinutesSelected: (Int) -> Unit,
    selectedDuration: Int?,
    onDurationSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "ResetLife") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = ResetLifeSpacing.screenHorizontal)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.lg),
        ) {
            when (step) {
                1 -> AreaStep(selectedArea, onAreaSelected)
                2 -> DurationStep(selectedMinutes, onMinutesSelected)
                3 -> PlanDurationStep(selectedDuration, onDurationSelected, onComplete = onNext)
            }
        }
    }
}

@Composable
private fun AreaStep(
    selectedArea: LifeArea?,
    onAreaSelected: (LifeArea) -> Unit,
) {
    ResetLifeSectionHeader(
        title = "Quais áreas da sua vida precisam de atenção?",
        supportingText = "Escolha uma área prioritária. Pode mudar depois.",
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(280.dp),
        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
        modifier = Modifier.padding(vertical = ResetLifeSpacing.sm),
    ) {
        items(LifeArea.entries) { area ->
            val isSelected = selectedArea == area
            Button(
                onClick = { onAreaSelected(area) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = when (area) {
                        LifeArea.Health -> "Saúde física"
                        LifeArea.Tasks -> "Tarefas/domínio"
                        LifeArea.Home -> "Casa/ambiente"
                        LifeArea.Finances -> "Finanças"
                        LifeArea.Relationships -> "Relacionamentos"
                        LifeArea.Mindset -> "Mentalidade/foco"
                        LifeArea.Energy -> "Energia/bem-estar"
                    },
                )
            }
        }
    }
}

@Composable
private fun DurationStep(
    selectedMinutes: Int?,
    onMinutesSelected: (Int) -> Unit,
) {
    ResetLifeSectionHeader(
        title = "Quanto tempo por dia?",
        supportingText = "Escolha quantos minutos podereá dedicar ao plano.",
    )

    Column(verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm)) {
        listOf(10, 20, 30, 45).forEach { minutes ->
            Button(
                onClick = { onMinutesSelected(minutes) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "${minutes} minutos")
            }
        }
    }
}

@Composable
private fun PlanDurationStep(
    selectedDuration: Int?,
    onDurationSelected: (Int) -> Unit,
    onComplete: () -> Unit,
) {
    ResetLifeSectionHeader(
        title = "Quantos dias de reset?",
        supportingText = "Escolha a duração do seu plano inicial.",
    )

    Column(verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm)) {
        listOf(7, 14, 30).forEach { days ->
            Button(
                onClick = { onDurationSelected(days) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "${days} dias")
            }
        }

        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedDuration != null,
        ) {
            Text(text = "Começar")
        }
    }
}
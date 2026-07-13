package br.com.resetlife.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.resetlife.domain.onboarding.LifeArea

@Composable
fun OnboardingScreen(
    selectedArea: LifeArea?,
    onAreaSelected: (LifeArea) -> Unit,
    onNext: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Quais áreas da sua vida precisam de atenção?")
        LifeArea.values().forEach { area ->
            Button(
                onClick = { onAreaSelected(area) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = area.name)
            }
        }
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedArea != null,
        ) {
            Text(text = "Próximo")
        }
    }
}
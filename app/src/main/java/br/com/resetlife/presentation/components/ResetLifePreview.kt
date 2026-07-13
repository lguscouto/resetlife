package br.com.resetlife.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.resetlife.presentation.theme.ResetLifeTheme

@Preview(showBackground = true, name = "ResetLife components - light")
@Composable
private fun ResetLifeComponentsLightPreview() {
    ResetLifeTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ResetLifeSectionHeader(
                title = "Prioridades de hoje",
                supportingText = "Escolha o próximo passo que importa.",
            )
            ResetLifeSurface(emphasized = true) {
                Text("Uma superfície de destaque", modifier = Modifier.padding(16.dp))
            }
            ResetLifeMessage("Tudo salvo localmente.", ResetLifeMessageTone.Success)
            ResetLifeMessage("Confira este campo.", ResetLifeMessageTone.Error)
        }
    }
}

@Preview(showBackground = true, name = "ResetLife components - dark")
@Composable
private fun ResetLifeComponentsDarkPreview() {
    ResetLifeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ResetLifeMessage("Atenção necessária.", ResetLifeMessageTone.Warning)
        }
    }
}

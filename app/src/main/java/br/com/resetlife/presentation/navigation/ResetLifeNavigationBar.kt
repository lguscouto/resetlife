package br.com.resetlife.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource

@Composable
fun ResetLifeNavigationBar(
    selectedDestination: ResetLifeDestination,
    onDestinationSelected: (ResetLifeDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        ResetLifeDestination.entries.forEach { destination ->
            val description = stringResource(destination.contentDescriptionRes)
            NavigationBarItem(
                selected = destination == selectedDestination,
                onClick = { onDestinationSelected(destination) },
                modifier = Modifier.semantics { contentDescription = description },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clearAndSetSemantics { contentDescription = description },
                    ) {
                        Text(
                            text = destination.symbol,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                },
                label = { Text(stringResource(destination.labelRes)) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }
    }
}

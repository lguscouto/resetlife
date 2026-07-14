package br.com.resetlife.presentation.customlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeEmptyState
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomListsScreen(
    state: CustomListsUiState,
    onSelectList: (String) -> Unit,
    onShowAddListDialog: () -> Unit,
    onHideAddListDialog: () -> Unit,
    onNewListNameChanged: (String) -> Unit,
    onAddList: () -> Unit,
    onShowAddItemDialog: () -> Unit,
    onHideAddItemDialog: () -> Unit,
    onNewItemTitleChanged: (String) -> Unit,
    onAddItem: () -> Unit,
    onToggleItem: (br.com.resetlife.domain.environment.CustomListItem) -> Unit,
    onDeleteItem: (String) -> Unit,
    onDeleteList: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ResetLifeSpacing.screenHorizontal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.lg),
    ) {
        ResetLifeSectionHeader(
            title = stringResource(R.string.custom_lists_title),
            supportingText = stringResource(R.string.custom_lists_subtitle),
        )

        OutlinedButton(onClick = onShowAddListDialog) {
            Text(stringResource(R.string.custom_lists_add))
        }

        if (state.lists.isEmpty()) {
            ResetLifeEmptyState(
                title = stringResource(R.string.empty_customlists),
                description = stringResource(R.string.empty_customlists_hint),
                actionLabel = stringResource(R.string.custom_lists_add),
                onAction = onShowAddListDialog,
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
            ) {
                state.lists.forEach { list ->
                    val selected = list.id == state.selectedListId
                    ResetLifeSurface(
                        modifier = Modifier
                            .clickable { onSelectList(list.id) },
                    ) {
                        Row(
                            modifier = Modifier.padding(ResetLifeSpacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                        ) {
                            Text(
                                text = list.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                            )
                            TextButton(onClick = { onDeleteList(list.id) }) {
                                Text(stringResource(R.string.delete))
                            }
                        }
                    }
                }
            }

            if (state.selectedListId != null) {
                ResetLifeSectionHeader(title = stringResource(R.string.custom_lists_items))
                OutlinedButton(onClick = onShowAddItemDialog) {
                    Text(stringResource(R.string.custom_lists_add_item))
                }
                if (state.items.isEmpty()) {
                    ResetLifeEmptyState(
                        title = stringResource(R.string.empty_customlists),
                        description = stringResource(R.string.custom_lists_items_empty),
                        actionLabel = stringResource(R.string.custom_lists_add_item),
                        onAction = onShowAddItemDialog,
                    )
                } else {
                    state.items.forEach { item ->
                        ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(ResetLifeSpacing.md),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
                            ) {
                                Checkbox(
                                    checked = item.done,
                                    onCheckedChange = { onToggleItem(item) },
                                )
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                )
                                TextButton(onClick = { onDeleteItem(item.id) }) {
                                    Text(stringResource(R.string.delete))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showAddListDialog) {
        AlertDialog(
            onDismissRequest = onHideAddListDialog,
            confirmButton = {
                TextButton(onClick = onAddList) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = onHideAddListDialog) { Text(stringResource(R.string.cancel)) }
            },
            title = { Text(stringResource(R.string.custom_lists_new_title)) },
            text = {
                TextField(
                    value = state.newListName,
                    onValueChange = onNewListNameChanged,
                    label = { Text(stringResource(R.string.custom_lists_name)) },
                    singleLine = true,
                )
            },
        )
    }

    if (state.showAddItemDialog) {
        AlertDialog(
            onDismissRequest = onHideAddItemDialog,
            confirmButton = {
                TextButton(onClick = onAddItem) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = onHideAddItemDialog) { Text(stringResource(R.string.cancel)) }
            },
            title = { Text(stringResource(R.string.custom_lists_new_item)) },
            text = {
                TextField(
                    value = state.newItemTitle,
                    onValueChange = onNewItemTitleChanged,
                    label = { Text(stringResource(R.string.custom_lists_item_title)) },
                    singleLine = true,
                )
            },
        )
    }
}

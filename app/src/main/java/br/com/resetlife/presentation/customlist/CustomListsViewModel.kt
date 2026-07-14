package br.com.resetlife.presentation.customlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.environment.EnvironmentRepository
import br.com.resetlife.domain.environment.CustomList
import br.com.resetlife.domain.environment.CustomListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomListsUiState(
    val lists: List<CustomList> = emptyList(),
    val selectedListId: String? = null,
    val items: List<CustomListItem> = emptyList(),
    val showAddListDialog: Boolean = false,
    val newListName: String = "",
    val showAddItemDialog: Boolean = false,
    val newItemTitle: String = "",
)

class CustomListsViewModel(private val repository: EnvironmentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomListsUiState())
    val uiState: StateFlow<CustomListsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeCustomLists().collect { lists ->
                val currentSelected = _uiState.value.selectedListId
                val selected = when {
                    currentSelected != null && lists.any { it.id == currentSelected } -> currentSelected
                    lists.isNotEmpty() -> lists.first().id
                    else -> null
                }
                _uiState.update { it.copy(lists = lists, selectedListId = selected) }
                if (selected != null) observeItems(selected)
            }
        }
    }

    private fun observeItems(listId: String) {
        viewModelScope.launch {
            repository.observeCustomListItems(listId).collect { items ->
                _uiState.update { it.copy(items = items) }
            }
        }
    }

    fun selectList(listId: String) {
        _uiState.update { it.copy(selectedListId = listId, items = emptyList()) }
        observeItems(listId)
    }

    fun showAddListDialog() = _uiState.update { it.copy(showAddListDialog = true) }
    fun hideAddListDialog() = _uiState.update { it.copy(showAddListDialog = false, newListName = "") }
    fun onNewListNameChanged(value: String) = _uiState.update { it.copy(newListName = value) }

    fun addList() {
        val name = _uiState.value.newListName.trim()
        if (name.isEmpty()) {
            hideAddListDialog()
            return
        }
        viewModelScope.launch {
            repository.addCustomList(name)
        }
        hideAddListDialog()
    }

    fun showAddItemDialog() = _uiState.update { it.copy(showAddItemDialog = true) }
    fun hideAddItemDialog() = _uiState.update { it.copy(showAddItemDialog = false, newItemTitle = "") }
    fun onNewItemTitleChanged(value: String) = _uiState.update { it.copy(newItemTitle = value) }

    fun addItem() {
        val state = _uiState.value
        val listId = state.selectedListId ?: run { hideAddItemDialog(); return }
        val title = state.newItemTitle.trim()
        if (title.isEmpty()) {
            hideAddItemDialog()
            return
        }
        viewModelScope.launch {
            repository.addCustomListItem(listId, title)
        }
        hideAddItemDialog()
    }

    fun toggleItem(item: CustomListItem) {
        viewModelScope.launch {
            repository.setCustomListItemDone(item, !item.done)
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            repository.deleteCustomListItem(itemId)
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            repository.deleteCustomList(listId)
        }
        val current = _uiState.value
        val remaining = current.lists.filter { it.id != listId }
        val next = if (current.selectedListId == listId) remaining.firstOrNull()?.id else current.selectedListId
        _uiState.update { it.copy(selectedListId = next, items = emptyList()) }
        if (next != null) observeItems(next)
    }
}

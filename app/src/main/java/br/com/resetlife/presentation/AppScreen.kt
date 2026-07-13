package br.com.resetlife.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.presentation.navigation.ResetLifeDestination
import br.com.resetlife.presentation.navigation.ResetLifeNavigationBar
import br.com.resetlife.presentation.organize.OrganizeScreen
import br.com.resetlife.presentation.organize.OrganizeViewModel
import br.com.resetlife.presentation.organize.OrganizeViewModelFactory
import br.com.resetlife.presentation.today.TodayScreen
import br.com.resetlife.presentation.today.TodayViewModel
import br.com.resetlife.presentation.today.TodayViewModelFactory

@Composable
fun ResetLifeApp(application: ResetLifeApplication) {
    var selectedKey by rememberSaveable { mutableStateOf(ResetLifeDestination.Today.key) }
    val selectedDestination = ResetLifeDestination.entries.firstOrNull { it.key == selectedKey }
        ?: ResetLifeDestination.Today

    BackHandler(enabled = selectedDestination != ResetLifeDestination.Today) {
        selectedKey = ResetLifeDestination.Today.key
    }

    val todayViewModel: TodayViewModel = viewModel(
        factory = TodayViewModelFactory(application.priorityStore),
    )
    val organizeViewModel: OrganizeViewModel = viewModel(
        factory = OrganizeViewModelFactory(application.organizeStore, application.priorityStore),
    )
    val todayState by todayViewModel.uiState.collectAsState()
    val organizeState by organizeViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ResetLifeNavigationBar(
                selectedDestination = selectedDestination,
                onDestinationSelected = { destination -> selectedKey = destination.key },
            )
        },
    ) { innerPadding ->
        when (selectedDestination) {
            ResetLifeDestination.Today -> TodayScreen(
                modifier = Modifier.padding(innerPadding),
                uiState = todayState,
                onPriorityInputChanged = todayViewModel::onPriorityInputChanged,
                onAddPriority = todayViewModel::addPriority,
                onCompletePriority = todayViewModel::completePriority,
                onRetry = todayViewModel::retryStorageOperation,
            )

            ResetLifeDestination.Organize -> OrganizeScreen(
                modifier = Modifier.padding(innerPadding),
                uiState = organizeState,
                onSearchChanged = organizeViewModel::onSearchChanged,
                onProjectTitleChanged = organizeViewModel::onProjectTitleChanged,
                onProjectGoalChanged = organizeViewModel::onProjectGoalChanged,
                onProjectSelected = organizeViewModel::onProjectSelected,
                onAddProject = organizeViewModel::addProject,
                onTaskTitleChanged = organizeViewModel::onTaskTitleChanged,
                onTaskNoteChanged = organizeViewModel::onTaskNoteChanged,
                onTaskDueDateChanged = organizeViewModel::onTaskDueDateChanged,
                onTaskEstimatedMinutesChanged = organizeViewModel::onTaskEstimatedMinutesChanged,
                onAddTask = organizeViewModel::addTask,
                onToggleTask = organizeViewModel::toggleTask,
                onPromoteToToday = organizeViewModel::promoteToToday,
                onRetry = organizeViewModel::retryStorageOperation,
            )
        }
    }
}

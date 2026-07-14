package br.com.resetlife.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import br.com.resetlife.presentation.navigation.bottomTabFor
import br.com.resetlife.presentation.onboarding.OnboardingScreen
import br.com.resetlife.presentation.onboarding.OnboardingUiState
import br.com.resetlife.presentation.onboarding.OnboardingViewModel
import br.com.resetlife.presentation.onboarding.OnboardingViewModelFactory
import br.com.resetlife.presentation.organize.OrganizeScreen
import br.com.resetlife.presentation.organize.OrganizeViewModel
import br.com.resetlife.presentation.organize.OrganizeViewModelFactory
import br.com.resetlife.presentation.today.TodayScreen
import br.com.resetlife.presentation.today.TodayViewModel
import br.com.resetlife.presentation.today.TodayViewModelFactory
import br.com.resetlife.presentation.wellbeing.CheckInScreen
import br.com.resetlife.presentation.wellbeing.CheckInViewModel
import br.com.resetlife.presentation.wellbeing.CheckInViewModelFactory
import br.com.resetlife.presentation.weeklyreview.WeeklyReviewScreen
import br.com.resetlife.presentation.weeklyreview.WeeklyReviewViewModel
import br.com.resetlife.presentation.weeklyreview.WeeklyReviewViewModelFactory
import br.com.resetlife.presentation.habit.HabitScreen
import br.com.resetlife.presentation.habit.HabitViewModel
import br.com.resetlife.presentation.habit.HabitViewModelFactory
import br.com.resetlife.presentation.habit.HabitDetailScreen
import br.com.resetlife.presentation.habit.HabitDetailViewModel
import br.com.resetlife.presentation.habit.HabitDetailViewModelFactory
import br.com.resetlife.presentation.environment.EnvironmentScreen
import br.com.resetlife.presentation.environment.EnvironmentViewModel
import br.com.resetlife.presentation.environment.EnvironmentViewModelFactory
import br.com.resetlife.presentation.customlist.CustomListsScreen
import br.com.resetlife.presentation.customlist.CustomListsViewModel
import br.com.resetlife.presentation.customlist.CustomListsViewModelFactory
import br.com.resetlife.presentation.life.LifeScreen
import br.com.resetlife.presentation.profile.DataExportViewModel
import br.com.resetlife.presentation.profile.DataExportViewModelFactory
import br.com.resetlife.presentation.profile.ProfileScreen

@Composable
fun ResetLifeApp(application: ResetLifeApplication) {
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(application.userProfileStore),
    )
    val userProfileState by onboardingViewModel.uiState.collectAsState(initial = OnboardingUiState())

    // Redireciona para onboarding se não completado
    val startDestination = if (userProfileState.onboardingCompleted) {
        ResetLifeDestination.Today
    } else {
        ResetLifeDestination.Onboarding
    }

    var selectedKey by rememberSaveable { mutableStateOf(startDestination.key) }
    val selectedDestination = ResetLifeDestination.entries.firstOrNull { it.key == selectedKey }
        ?: ResetLifeDestination.Today

    // Após concluir o onboarding, vai para Hoje automaticamente
    LaunchedEffect(userProfileState.onboardingCompleted) {
        if (userProfileState.onboardingCompleted && selectedKey == ResetLifeDestination.Onboarding.key) {
            selectedKey = ResetLifeDestination.Today.key
        }
    }

    BackHandler(enabled = selectedKey != ResetLifeDestination.Today.key && selectedKey != ResetLifeDestination.Onboarding.key) {
        // Se estiver num filho de hub, volta para o hub pai; se estiver no detalhe do
        // hábito, volta para a lista de hábitos; senão, volta para Hoje.
        val current = selectedDestination
        selectedKey = when {
            selectedKey.startsWith("habit_detail:") -> ResetLifeDestination.Habits.key
            !current.isBottomTab -> (current.parentTab ?: ResetLifeDestination.Today).key
            else -> ResetLifeDestination.Today.key
        }
    }

    val todayViewModel: TodayViewModel = viewModel(
        factory = TodayViewModelFactory(application.priorityStore, application.environmentStore),
    )
    val organizeViewModel: OrganizeViewModel = viewModel(
        factory = OrganizeViewModelFactory(application.organizeStore, application.priorityStore),
    )
    val checkInViewModel: CheckInViewModel = viewModel(
        factory = CheckInViewModelFactory(application.wellbeingStore),
    )
    val weeklyReviewViewModel: WeeklyReviewViewModel = viewModel(
        factory = WeeklyReviewViewModelFactory(
            application.priorityStore,
            application.organizeStore,
            application.weeklyReviewStore,
        ),
    )
    val habitViewModel: HabitViewModel = viewModel(
        factory = HabitViewModelFactory(application.habitStore),
    )
    val environmentViewModel: EnvironmentViewModel = viewModel(
        factory = EnvironmentViewModelFactory(application.environmentStore),
    )
    val customListsViewModel: CustomListsViewModel = viewModel(
        factory = CustomListsViewModelFactory(application.environmentStore),
    )
    val dataExportViewModel: DataExportViewModel = viewModel(
        factory = DataExportViewModelFactory(application),
    )
    val todayState by todayViewModel.uiState.collectAsState()
    val organizeState by organizeViewModel.uiState.collectAsState()
    val onboardingState by onboardingViewModel.uiState.collectAsState()
    val checkInState by checkInViewModel.uiState.collectAsState()
    val weeklyReviewState by weeklyReviewViewModel.uiState.collectAsState()
    val habitState by habitViewModel.uiState.collectAsState()
    val environmentState by environmentViewModel.uiState.collectAsState()
    val customListsState by customListsViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ResetLifeNavigationBar(
                selectedDestination = selectedDestination.bottomTabFor(),
                onDestinationSelected = { destination -> selectedKey = destination.key },
            )
        },
    ) { innerPadding ->
        val habitDetailId = if (selectedKey.startsWith("habit_detail:")) {
            selectedKey.removePrefix("habit_detail:")
        } else {
            null
        }
        if (habitDetailId != null) {
            val detailViewModel: HabitDetailViewModel = viewModel(
                key = "habit_detail_$habitDetailId",
                factory = HabitDetailViewModelFactory(habitDetailId, application.habitStore),
            )
            val detailState by detailViewModel.uiState.collectAsState()
            HabitDetailScreen(
                modifier = Modifier.padding(innerPadding),
                state = detailState,
                onBack = { selectedKey = ResetLifeDestination.Habits.key },
                onToggleToday = habitViewModel::toggleToday,
                onPause = habitViewModel::pause,
                onResume = habitViewModel::resume,
            )
        } else when (selectedDestination) {
            ResetLifeDestination.Today -> TodayScreen(
                modifier = Modifier.padding(innerPadding),
                uiState = todayState,
                onPriorityInputChanged = todayViewModel::onPriorityInputChanged,
                onAddPriority = todayViewModel::addPriority,
                onCompletePriority = todayViewModel::completePriority,
                onCompleteEnvironmentSuggestion = todayViewModel::completeEnvironmentSuggestion,
                onOpenEnvironment = { selectedKey = ResetLifeDestination.Environment.key },
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

            ResetLifeDestination.Life -> LifeScreen(
                modifier = Modifier.padding(innerPadding),
                onNavigate = { destination -> selectedKey = destination.key },
            )

            ResetLifeDestination.Profile -> ProfileScreen(
                modifier = Modifier.padding(innerPadding),
                themeManager = application.themeManager,
                onNavigate = { destination -> selectedKey = destination.key },
                dataExportViewModel = dataExportViewModel,
            )

            ResetLifeDestination.Onboarding -> OnboardingScreen(
                selectedArea = onboardingState.selectedArea,
                onAreaSelected = onboardingViewModel::selectArea,
                onNext = onboardingViewModel::nextStep,
                step = onboardingState.step,
                selectedMinutes = onboardingState.availableMinutes,
                onMinutesSelected = onboardingViewModel::setAvailableMinutes,
                selectedDuration = onboardingState.planDurationDays,
                onDurationSelected = onboardingViewModel::setPlanDuration,
            )

            ResetLifeDestination.Wellbeing -> CheckInScreen(
                modifier = Modifier.padding(innerPadding),
                state = checkInState,
                onMoodSelected = checkInViewModel::setMood,
                onEnergySelected = checkInViewModel::setEnergy,
                onStressSelected = checkInViewModel::setStress,
                onSleepSelected = checkInViewModel::setSleep,
                onSave = checkInViewModel::save,
            )

            ResetLifeDestination.WeeklyReview -> WeeklyReviewScreen(
                modifier = Modifier.padding(innerPadding),
                state = weeklyReviewState,
                onCompletedChanged = weeklyReviewViewModel::setCompleted,
                onPendingChanged = weeklyReviewViewModel::setPending,
                onDifficultyChanged = weeklyReviewViewModel::setDifficulty,
                onNextWeekChanged = weeklyReviewViewModel::setNextWeekPriorities,
                onHabitChanged = weeklyReviewViewModel::setHabitToAdjust,
                onSave = weeklyReviewViewModel::save,
            )

            ResetLifeDestination.Habits -> HabitScreen(
                modifier = Modifier.padding(innerPadding),
                state = habitState,
                onToggleToday = habitViewModel::toggleToday,
                onQuantityChanged = habitViewModel::setQuantityToday,
                onPause = habitViewModel::pause,
                onResume = habitViewModel::resume,
                onArchive = habitViewModel::archive,
                onOpenDetail = { habit -> selectedKey = "habit_detail:${habit.id}" },
                onShowAddDialog = habitViewModel::showAddDialog,
                onHideAddDialog = habitViewModel::hideAddDialog,
                onAddHabit = habitViewModel::addHabit,
                onClearReward = habitViewModel::clearReward,
            )

            ResetLifeDestination.Environment -> EnvironmentScreen(
                modifier = Modifier.padding(innerPadding),
                state = environmentState,
                onSelectSpace = environmentViewModel::selectSpace,
                onShowAddSpaceDialog = environmentViewModel::showAddSpaceDialog,
                onHideAddSpaceDialog = environmentViewModel::hideAddSpaceDialog,
                onNewSpaceNameChanged = environmentViewModel::onNewSpaceNameChanged,
                onAddSpace = environmentViewModel::addSpace,
                onShowAddTaskDialog = environmentViewModel::showAddTaskDialog,
                onHideAddTaskDialog = environmentViewModel::hideAddTaskDialog,
                onNewTaskTitleChanged = environmentViewModel::onNewTaskTitleChanged,
                onNewTaskMinutesChanged = environmentViewModel::onNewTaskMinutesChanged,
                onAddToDiscardListChanged = environmentViewModel::onAddToDiscardListChanged,
                onAddTask = environmentViewModel::addTask,
                onToggleTask = environmentViewModel::toggleTask,
            )

            ResetLifeDestination.CustomLists -> CustomListsScreen(
                modifier = Modifier.padding(innerPadding),
                state = customListsState,
                onSelectList = customListsViewModel::selectList,
                onShowAddListDialog = customListsViewModel::showAddListDialog,
                onHideAddListDialog = customListsViewModel::hideAddListDialog,
                onNewListNameChanged = customListsViewModel::onNewListNameChanged,
                onAddList = customListsViewModel::addList,
                onShowAddItemDialog = customListsViewModel::showAddItemDialog,
                onHideAddItemDialog = customListsViewModel::hideAddItemDialog,
                onNewItemTitleChanged = customListsViewModel::onNewItemTitleChanged,
                onAddItem = customListsViewModel::addItem,
                onToggleItem = customListsViewModel::toggleItem,
                onDeleteItem = customListsViewModel::deleteItem,
                onDeleteList = customListsViewModel::deleteList,
            )

            ResetLifeDestination.HabitDetail -> {
                // Detalhe do hábito é renderizado pelo bloco `if (habitDetailId != null)` acima.
            }
        }
    }
}

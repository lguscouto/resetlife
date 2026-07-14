package br.com.resetlife.presentation.weeklyreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.data.weeklyreview.WeeklyReviewStore

class WeeklyReviewViewModelFactory(
    private val priorityStore: PriorityStore,
    private val organizeStore: OrganizeStore,
    private val weeklyReviewStore: WeeklyReviewStore,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeeklyReviewViewModel(priorityStore, organizeStore, weeklyReviewStore) as T
    }
}

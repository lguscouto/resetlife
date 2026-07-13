package br.com.resetlife.domain.today

data class PriorityItem(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false,
)

class DailyPriorities private constructor(
    val items: List<PriorityItem>,
) {
    val activeCount: Int
        get() = items.count { !it.isCompleted }

    fun add(id: String, title: String): AddPriorityResult {
        val normalizedTitle = title.trim()
        if (normalizedTitle.isEmpty()) {
            return AddPriorityResult.EmptyTitle
        }
        if (activeCount >= MAX_ACTIVE_PRIORITIES) {
            return AddPriorityResult.LimitReached
        }

        return AddPriorityResult.Added(
            priorities = DailyPriorities(
                items = items + PriorityItem(id = id, title = normalizedTitle),
            ),
        )
    }

    fun complete(id: String): CompletionResult {
        if (items.none { item -> item.id == id }) {
            return CompletionResult.NotFound
        }

        return CompletionResult.Updated(
            priorities = DailyPriorities(
                items = items.map { item ->
                    if (item.id == id) item.copy(isCompleted = true) else item
                },
            ),
        )
    }

    companion object {
        const val MAX_ACTIVE_PRIORITIES = 3

        fun empty(): DailyPriorities = DailyPriorities(items = emptyList())

        fun from(items: List<PriorityItem>): DailyPriorities = DailyPriorities(items = items)
    }
}

sealed interface AddPriorityResult {
    data class Added(
        val priorities: DailyPriorities,
    ) : AddPriorityResult

    data object LimitReached : AddPriorityResult

    data object EmptyTitle : AddPriorityResult
}

sealed interface CompletionResult {
    data class Updated(
        val priorities: DailyPriorities,
    ) : CompletionResult

    data object NotFound : CompletionResult
}

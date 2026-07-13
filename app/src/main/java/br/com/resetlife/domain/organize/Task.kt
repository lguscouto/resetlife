package br.com.resetlife.domain.organize

enum class TaskStatus {
    NEXT_ACTION,
    SCHEDULED,
    SOMEDAY,
    COMPLETED,
}

data class Task(
    val id: String,
    val title: String,
    val note: String = "",
    val dueDate: String? = null,
    val estimatedMinutes: Int? = null,
    val projectId: String? = null,
    val status: TaskStatus = TaskStatus.NEXT_ACTION,
) {
    fun complete(): Task = copy(status = TaskStatus.COMPLETED)

    fun reopen(): Task = copy(status = TaskStatus.NEXT_ACTION)

    fun matchesQuery(query: String): Boolean {
        val normalizedQuery = query.trim()
        return normalizedQuery.isEmpty() || title.contains(normalizedQuery, ignoreCase = true)
    }

    companion object {
        fun create(
            id: String,
            title: String,
            note: String = "",
            dueDate: String? = null,
            estimatedMinutes: Int? = null,
            projectId: String? = null,
        ): TaskCreationResult {
            val normalizedTitle = title.trim()
            if (normalizedTitle.isEmpty()) return TaskCreationResult.EmptyTitle

            return TaskCreationResult.Created(
                Task(
                    id = id,
                    title = normalizedTitle,
                    note = note.trim(),
                    dueDate = dueDate?.trim()?.takeIf { it.isNotEmpty() },
                    estimatedMinutes = estimatedMinutes,
                    projectId = projectId,
                ),
            )
        }
    }
}

sealed interface TaskCreationResult {
    data class Created(val task: Task) : TaskCreationResult

    data object EmptyTitle : TaskCreationResult
}

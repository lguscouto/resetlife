package br.com.resetlife.domain.organize

data class Project(
    val id: String,
    val title: String,
    val goal: String = "",
) {
    companion object {
        fun create(id: String, title: String, goal: String = ""): ProjectCreationResult {
            val normalizedTitle = title.trim()
            if (normalizedTitle.isEmpty()) return ProjectCreationResult.EmptyTitle

            return ProjectCreationResult.Created(
                Project(
                    id = id,
                    title = normalizedTitle,
                    goal = goal.trim(),
                ),
            )
        }
    }
}

sealed interface ProjectCreationResult {
    data class Created(val project: Project) : ProjectCreationResult

    data object EmptyTitle : ProjectCreationResult
}

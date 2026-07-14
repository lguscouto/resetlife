package br.com.resetlife.domain.environment

/** Espaço físico da casa que pode ser organizado. */
data class EnvironmentSpace(
    val id: String,
    val name: String,
    val lastOrganizedAt: String? = null, // YYYY-MM-DD ou null
)

/** Tarefa de organização do ambiente. */
data class EnvironmentTask(
    val id: String,
    val spaceId: String,
    val title: String,
    val estimatedMinutes: Int, // 5, 15 ou 30
    val done: Boolean = false,
    val doneAt: String? = null, // YYYY-MM-DD ou null
    val discardList: Boolean = false, // true = item de descarte/doação
    val customListId: String? = null, // para listas próprias
)

/** Lista própria criada pelo usuário. */
data class CustomList(
    val id: String,
    val name: String,
)

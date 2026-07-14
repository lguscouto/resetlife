package br.com.resetlife.domain.habit

data class HabitLog(
    val id: String,
    val habitId: String,
    val date: String, // YYYY-MM-DD
    val value: Int, // para BINARY: 0/1; para QUANTITY: valor registrado
    val done: Boolean, // true se concluído (BINARY done=true quando value=1; QUANTITY done quando value>=target)
)

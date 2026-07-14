package br.com.resetlife.presentation.reward

import kotlin.random.Random

enum class RewardType {
    PRIORITY_DONE,
    HABIT_DONE,
    ALL_DONE,
}

object RewardProvider {
    private val messages: Map<RewardType, List<String>> = mapOf(
        RewardType.PRIORITY_DONE to listOf(
            "Prioridade concluída! Mais um passo dado.",
            "Boa! Você tirou do caminho.",
            "Pronto. Uma prioridade a menos no caminho.",
            "Concluído! Continue nesse ritmo.",
            "Mandou bem, prioridade fora da lista.",
            "Feito! Você escolheu agir.",
        ),
        RewardType.HABIT_DONE to listOf(
            "Hábito cumprido! Constância é tudo.",
            "Boa! Mais um dia de consistência.",
            "Concluído. Seu futuro agradece.",
            "Pronto! Pequeno gesto, grande efeito.",
            "Feito! A rotina está funcionando.",
            "Mandou bem, mais um hábito no dia.",
        ),
        RewardType.ALL_DONE to listOf(
            "Tudo concluído por hoje. Mandou bem!",
            "Dia zerado! Aproveite o descanso.",
            "Pronto: nada ficou pendente. Respire fundo.",
            "Tudo feito. Você cuidou do que importava.",
            "Concluído: dia encerrado com sucesso.",
            "Mandou bem! Hoje o plano fechou.",
        ),
    )

    fun randomReward(type: RewardType): String {
        val pool = messages.getValue(type)
        return pool[Random.nextInt(pool.size)]
    }

    fun rewardsFor(type: RewardType): List<String> = messages.getValue(type)
}

package br.com.resetlife.presentation.reward

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RewardProviderTest {

    @Test
    fun `randomReward returns non-empty string for every type`() {
        RewardType.values().forEach { type ->
            val result = RewardProvider.randomReward(type)
            assertFalse(
                "Reward for $type should not be empty",
                result.isBlank(),
            )
        }
    }

    @Test
    fun `randomReward returns a known phrase for every type`() {
        RewardType.values().forEach { type ->
            val known = RewardProvider.rewardsFor(type).toSet()
            val result = RewardProvider.randomReward(type)
            assertTrue(
                "Reward for $type should belong to its known pool",
                known.contains(result),
            )
        }
    }

    @Test
    fun `each reward type has at least five phrases`() {
        RewardType.values().forEach { type ->
            assertTrue(
                "Reward type $type should have at least 5 phrases",
                RewardProvider.rewardsFor(type).size >= 5,
            )
        }
    }

    @Test
    fun `rewards are stable across repeated calls within a type pool`() {
        val type = RewardType.HABIT_DONE
        val pool = RewardProvider.rewardsFor(type).toSet()
        repeat(20) {
            assertTrue(pool.contains(RewardProvider.randomReward(type)))
        }
    }
}

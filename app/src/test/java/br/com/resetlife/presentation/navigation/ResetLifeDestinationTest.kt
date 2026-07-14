package br.com.resetlife.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ResetLifeDestinationTest {
    @Test
    fun `destinations have stable distinct keys in public order`() {
        assertEquals(
            listOf(
                ResetLifeDestination.Today,
                ResetLifeDestination.Organize,
                ResetLifeDestination.Habits,
                ResetLifeDestination.WeeklyReview,
                ResetLifeDestination.Onboarding,
                ResetLifeDestination.Wellbeing,
            ),
            ResetLifeDestination.entries,
        )
        assertNotEquals(ResetLifeDestination.Today.key, ResetLifeDestination.Organize.key)
    }
}
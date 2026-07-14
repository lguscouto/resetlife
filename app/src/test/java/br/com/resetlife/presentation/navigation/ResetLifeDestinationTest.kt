package br.com.resetlife.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResetLifeDestinationTest {
    @Test
    fun `destinations have stable distinct keys in public order`() {
        assertEquals(
            listOf(
                ResetLifeDestination.Today,
                ResetLifeDestination.Organize,
                ResetLifeDestination.Life,
                ResetLifeDestination.Profile,
                ResetLifeDestination.Habits,
                ResetLifeDestination.Environment,
                ResetLifeDestination.CustomLists,
                ResetLifeDestination.Wellbeing,
                ResetLifeDestination.WeeklyReview,
                ResetLifeDestination.HabitDetail,
                ResetLifeDestination.Onboarding,
            ),
            ResetLifeDestination.entries,
        )
        assertNotEquals(ResetLifeDestination.Today.key, ResetLifeDestination.Organize.key)
    }

    @Test
    fun `bottom bar shows at most four tabs`() {
        assertTrue(bottomTabs.size <= 4)
        assertEquals(
            listOf(
                ResetLifeDestination.Today,
                ResetLifeDestination.Organize,
                ResetLifeDestination.Life,
                ResetLifeDestination.Profile,
            ),
            bottomTabs,
        )
    }

    @Test
    fun `child destinations map to their parent tab`() {
        assertEquals(ResetLifeDestination.Life, ResetLifeDestination.Habits.bottomTabFor())
        assertEquals(ResetLifeDestination.Life, ResetLifeDestination.Wellbeing.bottomTabFor())
        assertEquals(ResetLifeDestination.Profile, ResetLifeDestination.WeeklyReview.bottomTabFor())
        assertEquals(ResetLifeDestination.Today, ResetLifeDestination.Today.bottomTabFor())
    }
}

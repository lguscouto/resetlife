package br.com.resetlife.data.local

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResetLifeDatabaseMigrationTest {
    private companion object {
        const val DATABASE_NAME = "resetlife-migration-test"
    }

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ResetLifeDatabase::class.java,
        emptyList(),
    )

    @Test
    @Throws(IOException::class)
    fun migratesVersionOnePrioritiesWithoutLosingData() {
        helper.createDatabase(DATABASE_NAME, 1).apply {
            execSQL(
                """
                INSERT INTO priorities (id, title, isCompleted, dayKey, createdAt)
                VALUES ('p1', 'Prioridade antiga', 0, '2026-07-12', 0)
                """.trimIndent(),
            )
            close()
        }

        val database = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            ResetLifeDatabase::class.java,
            DATABASE_NAME,
        ).build()
        val cursor = database.openHelper.writableDatabase.query(
            "SELECT title FROM priorities WHERE id = 'p1'",
        )

        cursor.use {
            assertEquals(true, it.moveToFirst())
            assertEquals("Prioridade antiga", it.getString(0))
        }
        database.close()
    }
}

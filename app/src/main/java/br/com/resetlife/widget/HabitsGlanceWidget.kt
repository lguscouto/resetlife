package br.com.resetlife.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider
import androidx.compose.ui.graphics.Color
import br.com.resetlife.R
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.domain.habit.Habit
import kotlinx.coroutines.flow.first
import java.time.LocalDate

private val HabitKey = ActionParameters.Key<String>("habitId")

class HabitsGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as ResetLifeApplication
        val habits = app.habitStore.observeHabits().first()
        val today = LocalDate.now().toString()
        val doneIds = app.habitStore
            .observeAllLogsForDate(today)
            .first()
            .map { it.habitId }
            .toSet()
        provideContent {
            HabitsContent(habits, doneIds)
        }
    }

    @Composable
    private fun HabitsContent(habits: List<Habit>, doneIds: Set<String>) {
        val res = LocalContext.current.resources
        val visible = habits
            .filter { it.active && !it.paused }
            .take(WIDGET_MAX_HABITS)
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(surfaceColor())
                .padding(ResetLifeWidgetDimens.padding),
            verticalAlignment = Alignment.Vertical.Top,
        ) {
            Text(
                text = res.getString(R.string.widget_habits_title),
                style = TextStyle(
                    fontSize = ResetLifeWidgetDimens.title,
                    fontWeight = FontWeight.Bold,
                    color = contentColor(),
                ),
            )
            Spacer(modifier = GlanceModifier.height(ResetLifeWidgetDimens.gap))
            if (visible.isEmpty()) {
                Text(
                    text = res.getString(R.string.widget_habits_empty),
                    style = TextStyle(
                        fontSize = ResetLifeWidgetDimens.body,
                        color = contentColor(),
                    ),
                )
            } else {
                visible.forEach { habit ->
                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(vertical = ResetLifeWidgetDimens.itemGap),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                    ) {
                        Text(
                            text = habit.name,
                            modifier = GlanceModifier.defaultWeight(),
                            style = TextStyle(
                                fontSize = ResetLifeWidgetDimens.body,
                                color = contentColor(),
                            ),
                        )
                        Spacer(modifier = GlanceModifier.width(ResetLifeWidgetDimens.gap))
                        val done = doneIds.contains(habit.id)
                        androidx.glance.Button(
                            text = if (done) res.getString(R.string.widget_done_mark) else res.getString(R.string.widget_complete),
                            onClick = actionRunCallback<ToggleHabitAction>(
                                parameters = actionParametersOf(HabitKey to habit.id),
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun surfaceColor() = ColorProvider(Color(0xFFF7F8F6), Color(0xFF141816))

    private fun contentColor() = ColorProvider(Color(0xFF202522), Color(0xFFE0E5E1))

    companion object {
        const val WIDGET_MAX_HABITS = 4
    }
}

class HabitsGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HabitsGlanceWidget()
}

class ToggleHabitAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val id = parameters[HabitKey] ?: return
        val app = context.applicationContext as ResetLifeApplication
        WidgetActions.toggleHabit(
            observeHabits = { app.habitStore.observeHabits() },
            toggleToday = { app.habitStore.toggleToday(it) },
            id = id,
        )
        HabitsGlanceWidget().update(context, glanceId)
    }
}

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
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.today.PriorityItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

private val PriorityKey = ActionParameters.Key<String>("priorityId")

object ResetLifeWidgetDimens {
    val padding = Dp(16f)
    val gap = Dp(8f)
    val itemGap = Dp(4f)
    val body = TextUnit(14f, Sp)
    val title = TextUnit(18f, Sp)
}

/**
 * Lógica de ação isolada e testável, sem dependência de Android/Context.
 */
object WidgetActions {
    suspend fun completePriority(store: PriorityStore, id: String): Boolean = store.complete(id)

    suspend fun toggleHabit(
        observeHabits: () -> Flow<List<br.com.resetlife.domain.habit.Habit>>,
        toggleToday: suspend (br.com.resetlife.domain.habit.Habit) -> Unit,
        id: String,
    ): Boolean {
        val habit = observeHabits().first().firstOrNull { it.id == id } ?: return false
        toggleToday(habit)
        return true
    }
}

class TodayGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as ResetLifeApplication
        val priorities = app.priorityStore.observeToday().first()
        provideContent {
            TodayContent(priorities)
        }
    }

    @Composable
    private fun TodayContent(priorities: List<PriorityItem>) {
        val res = LocalContext.current.resources
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(surfaceColor())
                .padding(ResetLifeWidgetDimens.padding),
            verticalAlignment = Alignment.Vertical.Top,
        ) {
            Text(
                text = res.getString(R.string.widget_today_title),
                style = TextStyle(
                    fontSize = ResetLifeWidgetDimens.title,
                    fontWeight = FontWeight.Bold,
                    color = contentColor(),
                ),
            )
            Spacer(modifier = GlanceModifier.height(ResetLifeWidgetDimens.gap))
            if (priorities.isEmpty()) {
                Text(
                    text = res.getString(R.string.widget_empty),
                    style = TextStyle(
                        fontSize = ResetLifeWidgetDimens.body,
                        color = contentColor(),
                    ),
                )
            } else {
                priorities.take(WIDGET_MAX_PRIORITIES).forEach { priority ->
                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(vertical = ResetLifeWidgetDimens.itemGap),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                    ) {
                        Text(
                            text = priority.title,
                            modifier = GlanceModifier.defaultWeight(),
                            style = TextStyle(
                                fontSize = ResetLifeWidgetDimens.body,
                                color = contentColor(),
                            ),
                        )
                        Spacer(modifier = GlanceModifier.width(ResetLifeWidgetDimens.gap))
                        androidx.glance.Button(
                            text = res.getString(R.string.widget_complete),
                            onClick = actionRunCallback<CompletePriorityAction>(
                                parameters = actionParametersOf(PriorityKey to priority.id),
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
        const val WIDGET_MAX_PRIORITIES = 3
    }
}

class TodayGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodayGlanceWidget()
}

class CompletePriorityAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val id = parameters[PriorityKey] ?: return
        val app = context.applicationContext as ResetLifeApplication
        WidgetActions.completePriority(app.priorityStore, id)
        TodayGlanceWidget().update(context, glanceId)
    }
}

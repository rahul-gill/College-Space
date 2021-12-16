package com.artisticent.collegespace.presentation.ui.events.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt


@Composable
fun BasicSchedule(
    events: List<UiEvent>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (positionedEvent: PositionedEvent) -> Unit = { BasicEvent(positionedEvent = it, showTimeOfEvents = showTimeOfEvents) },
    minDate: LocalDate = events.minByOrNull(UiEvent::start)?.start?.toLocalDate() ?: LocalDate.now(),
    maxDate: LocalDate = events.maxByOrNull(UiEvent::end)?.end?.toLocalDate() ?: LocalDate.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    dayWidth: Dp,
    hourHeight: Dp,
    showTimeOfEvents: Boolean = true
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val numMinutes = ChronoUnit.MINUTES.between(minTime, maxTime).toInt() + 1
    val numHours = numMinutes / 60
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val positionedEvents = remember(events) { arrangeEvents(splitEvents(events.sortedBy(UiEvent::start))).filter { it.end > minTime && it.start < maxTime } }
    Layout(
        content = {
            positionedEvents.forEach { positionedEvent ->
                Box(modifier = Modifier.eventData(positionedEvent)) {
                    eventContent(positionedEvent)
                }
            }
        },
        modifier = modifier
            .drawBehind {
                val firstHour = minTime.truncatedTo(ChronoUnit.HOURS)
                val firstHourOffsetMinutes = if (firstHour == minTime) 0 else ChronoUnit.MINUTES.between(minTime, firstHour.plusHours(1))
                val firstHourOffset = (firstHourOffsetMinutes / 60f) * hourHeight.toPx()
                repeat(numHours) {
                    drawLine(
                        dividerColor,
                        start = Offset(0f, it * hourHeight.toPx() + firstHourOffset),
                        end = Offset(size.width, it * hourHeight.toPx() + firstHourOffset),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                repeat(numDays - 1) {
                    drawLine(
                        dividerColor,
                        start = Offset((it + 1) * dayWidth.toPx(), 0f),
                        end = Offset((it + 1) * dayWidth.toPx(), size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) { measureables, constraints ->
        val height = (hourHeight.toPx() * (numMinutes / 60f)).roundToInt()
        val width = dayWidth.roundToPx() * numDays
        val placeablesWithEvents = measureables.map { measurable ->
            val splitEvent = measurable.parentData as PositionedEvent
            val eventDurationMinutes = ChronoUnit.MINUTES.between(splitEvent.start, minOf(splitEvent.end, maxTime))
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val eventWidth = ((splitEvent.colSpan.toFloat() / splitEvent.colTotal.toFloat()) * dayWidth.toPx()).roundToInt()
            val placeable = measurable.measure(constraints.copy(minWidth = eventWidth, maxWidth = eventWidth, minHeight = eventHeight, maxHeight = eventHeight))
            Pair(placeable, splitEvent)
        }
        layout(width, height) {
            placeablesWithEvents.forEach { (placeable, splitEvent) ->
                val eventOffsetMinutes = if (splitEvent.start > minTime) ChronoUnit.MINUTES.between(minTime, splitEvent.start) else 0
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val eventOffsetDays = ChronoUnit.DAYS.between(minDate, splitEvent.date).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx() + (splitEvent.col * (dayWidth.toPx() / splitEvent.colTotal.toFloat())).roundToInt()
                placeable.place(eventX, eventY)
            }
        }
    }
}

package com.artisticent.collegespace.presentation.ui.events.schedule

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artisticent.collegespace.presentation.AppTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit


@Composable
fun Schedule(
    events: List<Event>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (positionedEvent: PositionedEvent) -> Unit = { BasicEvent(positionedEvent = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
    timeLabel: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
    minDate: LocalDate = events.minByOrNull(Event::start)?.start?.toLocalDate() ?: LocalDate.now(),
    maxDate: LocalDate = events.maxByOrNull(Event::end)?.end?.toLocalDate() ?: LocalDate.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    daySize: ScheduleSize = ScheduleSize.FixedSize(256.dp),
    hourSize: ScheduleSize = ScheduleSize.FixedSize(64.dp),
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val numMinutes = ChronoUnit.MINUTES.between(minTime, maxTime).toInt() + 1
    val numHours = numMinutes.toFloat() / 60f
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    var headerHeight by remember { mutableStateOf(0) }
    var scale by remember { mutableStateOf(1f) }
    val transformableState = rememberTransformableState { zoomChange, _ , _ -> scale *= zoomChange }

    var boxSize = 0
    BoxWithConstraints(modifier = modifier
        .transformable(transformableState)
    ) {
        val dayWidth: Dp = when (daySize) {
            is ScheduleSize.FixedSize -> daySize.size
            is ScheduleSize.FixedCount -> with(LocalDensity.current) { ((constraints.maxWidth - sidebarWidth) / daySize.count).toDp() }
            is ScheduleSize.Adaptive -> with(LocalDensity.current) { maxOf(((constraints.maxWidth - sidebarWidth) / numDays).toDp(), daySize.minSize) }
        }.times(scale)
        val hourHeight: Dp = when (hourSize) {
            is ScheduleSize.FixedSize -> hourSize.size
            is ScheduleSize.FixedCount -> with(LocalDensity.current) { ((constraints.maxHeight - headerHeight) / hourSize.count).toDp() }
            is ScheduleSize.Adaptive -> with(LocalDensity.current) { maxOf(((constraints.maxHeight - headerHeight) / numHours).toDp(), hourSize.minSize) }
        }
        Column(modifier = modifier) {
            ScheduleHeader(
                minDate = minDate,
                maxDate = maxDate,
                dayWidth = dayWidth,
                dayHeader = dayHeader,
                modifier = Modifier
                    .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
                    .horizontalScroll(horizontalScrollState)
                    .onGloballyPositioned { headerHeight = it.size.height }
            )
            Row(modifier = Modifier
                .weight(1f)
                .align(Alignment.Start)) {
                ScheduleSidebar(
                    hourHeight = hourHeight,
                    minTime = minTime,
                    maxTime = maxTime,
                    label = timeLabel,
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                        .onGloballyPositioned { sidebarWidth = it.size.width }
                )
                BasicSchedule(
                    events = events,
                    eventContent = eventContent,
                    minDate = minDate,
                    maxDate = maxDate,
                    minTime = minTime,
                    maxTime = maxTime,
                    dayWidth = dayWidth,
                    hourHeight = hourHeight,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(verticalScrollState)
                        .horizontalScroll(horizontalScrollState)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchedulePreview() {
    AppTheme {
        Schedule(sampleEvents)
    }
}
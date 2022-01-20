package com.github.rahul_gill.collegespace.presentation.ui.events.schedule

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.github.rahul_gill.collegespace.data.room.entities.PersonalEventEntity
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.domain.models.AttendanceRecord
import com.github.rahul_gill.collegespace.util.Util
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


data class UiEvent(
    var name: String = "",
    var color: Color = Color.Cyan,
    var start: LocalDateTime = LocalDateTime.now(),
    var end: LocalDateTime = LocalDateTime.now(),
    var description: String? = null,
    var attendanceRecord: AttendanceRecord? = null
){
    fun toPersonalEventEntity() = PersonalEventEntity(
        eventName =  name,
        start = Util.toDate(start),
        end = Util.toDate(end),
        description = description
    )

    companion object{
        fun from(event: Event): UiEvent {
            return UiEvent(
                name = when{
                    event.className != null -> event.className!!
                    event.userGroupName != null -> event.userGroupName!!
                    else -> event.eventName
                },
                color = when{
                    event.className != null -> Color.Red
                    event.userGroupName != null -> Color.Green
                    else -> Color.Cyan
                },
                start = Util.latestLocalDateTime(event.start),
                end = event.duration?.let { Util.datePlusDuration(event.start,  it.toDuration()) } ?: LocalDateTime.now(),
                description = event.description,
                attendanceRecord = event.attendanceRecord
            )
        }
        fun from(event: PersonalEventEntity): UiEvent{
            return UiEvent(
                name = event.eventName,
                start = Util.latestLocalDateTime(event.start, event.repeatPeriod),
                end = Util.latestLocalDateTime(event.end, event.repeatPeriod),
                description = event.description
            )
        }
    }
}


@JvmInline
value class SplitType private constructor(val value: Int) {
    companion object {
        val None = SplitType(0)
        val Start = SplitType(1)
        val End = SplitType(2)
        val Both = SplitType(3)
    }
}

data class PositionedEvent(
    val event: UiEvent,
    val splitType: SplitType,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
    val col: Int = 0,
    val colSpan: Int = 1,
    val colTotal: Int = 1,
)




internal class EventDataModifier(
    private val positionedEvent: PositionedEvent,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = positionedEvent
}

internal fun Modifier.eventData(positionedEvent: PositionedEvent) = this.then(EventDataModifier(positionedEvent))

internal val DayFormatter = DateTimeFormatter.ofPattern("EE\ndMMM")
internal val SingleLineDayFormatter = DateTimeFormatter.ofPattern("EE:dMMM")



sealed class ScheduleSize {
    class FixedSize(val size: Dp) : ScheduleSize()
    class FixedCount(val count: Float) : ScheduleSize() {
        constructor(count: Int) : this(count.toFloat())
    }
    class Adaptive(val minSize: Dp) : ScheduleSize()
}
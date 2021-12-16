package com.artisticent.collegespace.presentation.ui.events.schedule

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.artisticent.collegespace.presentation.AppTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DaySchedule() = AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            Schedule(
                events = sampleEvents,
                maxDate = LocalDate.now(),
                oneLineHeader = true
            )
        }
}

@Preview(showBackground = true)
@Composable
fun WeekSchedule(
    events: List<UiEvent> = sampleEvents
) = AppTheme {
    Surface(color = MaterialTheme.colors.background) {
        Schedule(
            events = events,
            minDate = LocalDate.now().with(DayOfWeek.MONDAY),
            maxDate = LocalDate.now().with(DayOfWeek.MONDAY).plusDays(14),
            daySize = ScheduleSize.FixedSize(60.dp),
            oneLineHeader = false,
            showTimeOfEvents = false
        )
    }
}


val EventTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

class EventsProvider : PreviewParameterProvider<UiEvent> {
    override val values = sampleEvents.asSequence()
}

val sampleEvents = listOf(
    UiEvent(
        name = "Google I/O Keynote",
        color = Color(0xFFAFBBF2),
        start = LocalDateTime.parse("2021-12-16T09:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "Tune in to find out about how we're furthering our mission to organize the world’s information and make it universally accessible and useful.",
    ),
    UiEvent(
        name = "Developer Keynote",
        color = Color(0xFFAFBBF2),
        start = LocalDateTime.parse("2021-05-18T09:00:00"),
        end = LocalDateTime.parse("2021-05-18T10:00:00"),
        description = "Learn about the latest updates to our developer products and platforms from Google Developers.",
    ),
    UiEvent(
        name = "What's new in Android",
        color = Color(0xFF1B998B),
        start = LocalDateTime.parse("2021-05-18T10:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "In this Keynote, Chet Haase, Dan Sandler, and Romain Guy discuss the latest Android features and enhancements for developers.",
    ),
    UiEvent(
        name = "What's new in Material Design",
        color = Color(0xFF6DD3CE),
        start = LocalDateTime.parse("2021-05-18T11:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:55:00"),
        description = "Learn about the latest design improvements to help you build personal dynamic experiences with Material Design.",
    ),
    UiEvent(
        name = "What's new in Machine Learning",
        color = Color(0xFFF4BFDB),
        start = LocalDateTime.parse("2021-05-18T10:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "Learn about the latest and greatest in ML from Google. We’ll cover what’s available to developers when it comes to creating, understanding, and deploying models for a variety of different applications.",
    ),
    UiEvent(
        name = "What's new in Machine Learning",
        color = Color(0xFFF4BFDB),
        start = LocalDateTime.parse("2021-05-18T10:30:00"),
        end = LocalDateTime.parse("2021-05-18T11:30:00"),
        description = "Learn about the latest and greatest in ML from Google. We’ll cover what’s available to developers when it comes to creating, understanding, and deploying models for a variety of different applications.",
    ),
    UiEvent(
        name = "Jetpack Compose Basics",
        color = Color(0xFF1B998B),
        start = LocalDateTime.parse("2021-05-20T12:00:00"),
        end = LocalDateTime.parse("2021-05-20T13:00:00"),
        description = "This Workshop will take you through the basics of building your first app with Jetpack Compose, Android's new modern UI toolkit that simplifies and accelerates UI development on Android.",
    ),
)





internal val HourFormatter = DateTimeFormatter.ofPattern("h a")




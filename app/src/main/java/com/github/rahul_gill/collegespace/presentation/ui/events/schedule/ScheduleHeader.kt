package com.github.rahul_gill.collegespace.presentation.ui.events.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.rahul_gill.collegespace.presentation.theme.AppTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun BasicDayHeader(
    day: LocalDate,
    modifier: Modifier = Modifier,
    oneLine: Boolean = true
) {
    Text(
        text = day.format(if(oneLine) SingleLineDayFormatter else  DayFormatter),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ScheduleHeader(
    minDate: LocalDate,
    maxDate: LocalDate,
    dayWidth: Dp,
    modifier: Modifier = Modifier,
    oneLine: Boolean = true,
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it, oneLine = oneLine) },
) {
    Row(modifier = modifier) {
        val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
        repeat(numDays) { i ->
            Box(modifier = Modifier.width(dayWidth)) {
                dayHeader(minDate.plusDays(i.toLong()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicDayHeaderPreview() {
    AppTheme {
        BasicDayHeader(day = LocalDate.now())
    }
}



@Preview(showBackground = true)
@Composable
fun ScheduleHeaderPreview() {
    AppTheme {
        ScheduleHeader(
            minDate = LocalDate.now(),
            maxDate = LocalDate.now().plusDays(5),
            dayWidth = 256.dp,
        )
    }
}
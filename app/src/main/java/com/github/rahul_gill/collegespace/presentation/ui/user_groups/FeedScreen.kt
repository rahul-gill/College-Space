package com.github.rahul_gill.collegespace.presentation.ui.user_groups

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.rahul_gill.collegespace.domain.models.DurationWrapper
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.presentation.theme.AppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Duration
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Preview
@Composable
fun FeedScreen(
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    posts: List<Event> = List(5){
        Event(
            eventName = "Contest",
            start = Calendar.getInstance().time,
            duration = DurationWrapper.from(Duration.ofHours(5))  ,
            description = "A programming contest is being held here today",
            userGroupName = "coding_guys"
        )
    },
    onAddToCalendar: (Event) -> Unit = {},
    onCreateNewPost: () -> Unit= {}
) = AppTheme{
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateNewPost() },
                content = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            )
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { onRefresh() },
                Modifier.background(MaterialTheme.colors.surface).fillMaxHeight().fillMaxWidth()
            ) {
                if (posts.isEmpty() && !isRefreshing) {
                    Text(
                        text = "There are no posts currently",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                } else LazyColumn {
                    items(posts.size) { index ->
                        Post(postModel = posts[index], onAddToCalendar = onAddToCalendar)
                    }
                }
            }
        }
    )
}

@Composable
fun Post(
    postModel: Event,
    onAddToCalendar: (Event) -> Unit
) = Card(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp).fillMaxWidth()){
    Box {
        Column {
            Text(
                text = postModel.eventName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
            )
            Text(
                text = postModel.userGroupName!!,
                style = MaterialTheme.typography.caption.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            val start = postModel.start.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val end = if (postModel.duration == null) null else start.plus(postModel.duration!!.toDuration())
            val formatter = DateTimeFormatter.ofPattern("EEE, MMM d HH:MM a")
            val formatter1 = DateTimeFormatter.ofPattern("HH:MM a")
            val timeString =
                if (end == null) start.format(formatter)
                else start.format(formatter) + " to " + end.format(formatter1)
            Text(
                text = timeString.uppercase(),
                style = MaterialTheme.typography.caption.copy(
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
            if (!postModel.description.isNullOrEmpty()) {
                ExpandingText(
                    text = postModel.description!!,
                    style = MaterialTheme.typography.body1.copy(fontSize = 12.sp),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 4.dp)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
                .clickable { onAddToCalendar(postModel) }
                .align(Alignment.TopEnd)
        )
    }
}

const val MINIMIZED_MAX_LINES = 3

@Composable
fun ExpandingText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle
) {
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(AnnotatedString(text)) }

    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                finalText = buildAnnotatedString {
                    append(text)
                    withStyle(SpanStyle(color = Color(0xFF58a6ff))){
                        append(" show less")
                    }
                }
            }
            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(MINIMIZED_MAX_LINES - 1)
                val showMoreString = "... Show More"
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                    .dropLastWhile { it == ' ' || it == '.' }

                finalText = buildAnnotatedString {
                    append(adjustedText)
                    withStyle(SpanStyle(color = Color.Blue)){
                        append(showMoreString)
                    }
                }

                isClickable = true
            }
        }
    }

    Text(
        text = finalText,
        maxLines = if (isExpanded) Int.MAX_VALUE else MINIMIZED_MAX_LINES,
        onTextLayout = { textLayoutResultState.value = it },
        style = style,
        modifier = modifier
            .clickable(enabled = isClickable) { isExpanded = !isExpanded }
            .animateContentSize(),
    )
}
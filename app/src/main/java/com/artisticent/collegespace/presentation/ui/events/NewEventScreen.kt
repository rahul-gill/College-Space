package com.artisticent.collegespace.presentation.ui.events

import android.widget.CalendarView
import android.widget.TimePicker
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.artisticent.collegespace.presentation.AppTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class EventInstantPickItems {
    SELECTED_TODAY,
    SELECTED_TOMORROW,
    SELECTED_PICK_DATE,
    SELECTED_MORNING,
    SELECTED_EVENING,
    SELECTED_PICK_TIME,
}

enum class Picker{
    DATE_PICKER, TIME_PICKER
}
enum class StartOrEnd{
    START_TIME, END_TIME
}

val viewFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy hh:mm a")

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun NewEventScreen(onDone: (String,LocalDateTime,LocalDateTime,String) -> Unit) = AppTheme{
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    var selectedStart by remember { mutableStateOf(LocalDateTime.now().withHour(8).withMinute(0)) }
    var selectedEnd by remember { mutableStateOf(LocalDateTime.now().withHour(8).withMinute(0)) }
    var startOrEnd by remember { mutableStateOf(StartOrEnd.START_TIME) }
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box{
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 8.dp, top = 4.dp)
                        .size(30.dp)
                        .clickable {
                            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                        }
                )
                BottomSheet(updateDateTime = {
                    when(startOrEnd){
                        StartOrEnd.START_TIME -> selectedStart = it
                        StartOrEnd.END_TIME -> selectedEnd = it
                    }
                })
            }
       },
        sheetPeekHeight = 0.dp
    ) {
        var eventName by remember { mutableStateOf("Event Name") }
        var eventDescription by remember { mutableStateOf("") }
        Column{
            val keyboardController = LocalSoftwareKeyboardController.current
            Surface(
                color = MaterialTheme.colors.primary,
            ) {
                TextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    textStyle = TextStyle(fontSize = 40.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onPrimary
                    )
                )
            }
            Row (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            startOrEnd = StartOrEnd.START_TIME
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Filled.PendingActions, contentDescription = "", modifier = Modifier.padding(end=4.dp))
                Text(text = "Start of Event\n${selectedStart.format(viewFormat)}")
            }
            Divider(thickness = 2.dp)
            Row (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            startOrEnd = StartOrEnd.END_TIME
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Filled.PendingActions, contentDescription = "", modifier = Modifier.padding(end=4.dp))
                Text(text = "End of Event\n${selectedEnd.format(viewFormat)}")
            }
            Divider(thickness = 2.dp)
            OutlinedTextField(
                value = eventDescription,
                label = { Text("Event Description") },
                onValueChange = { eventDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp)
                    .padding(16.dp)
            )
            Button(
                onClick = {
                    onDone(eventName, selectedStart, selectedEnd, eventDescription)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "")
                Text("Done")
            }
        }
    }
}

@Composable
fun BottomSheet(updateDateTime: (LocalDateTime) -> Unit) {
    var selectedLocalDate by remember { mutableStateOf(LocalDateTime.now().withHour(8).withMinute(0)) }
    var selectedDateItem by remember { mutableStateOf(EventInstantPickItems.SELECTED_TODAY) }
    var selectedTimeItem by remember { mutableStateOf(EventInstantPickItems.SELECTED_MORNING) }
    var currentShownPicker by remember { mutableStateOf(Picker.DATE_PICKER) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DatePick(selectedDateItem, onClick = {
                selectedDateItem = it
                if (it != EventInstantPickItems.SELECTED_PICK_DATE)
                    selectedLocalDate = dateFromPredefinedItems(it, selectedLocalDate)
                else
                    currentShownPicker = Picker.DATE_PICKER
                updateDateTime(selectedLocalDate)
            })
            TimePick(selectedTimeItem, onClick = {
                selectedTimeItem = it
                if (it != EventInstantPickItems.SELECTED_PICK_TIME)
                    selectedLocalDate = dateFromPredefinedItems(it, selectedLocalDate)
                else
                    currentShownPicker = Picker.TIME_PICKER
                updateDateTime(selectedLocalDate)
            })
        }
        Text(
            text = selectedLocalDate.format(viewFormat)
        )

        Crossfade(
            modifier = Modifier
                .height(400.dp),
            targetState = currentShownPicker,
        ) { screen ->
            when (screen) {
                Picker.DATE_PICKER -> MyDatePicker(onDateChange = { year, mon, dom ->
                    selectedLocalDate =
                        selectedLocalDate.withYear(year).withMonth(mon+1).withDayOfMonth(dom)
                    updateDateTime(selectedLocalDate)
                })
                Picker.TIME_PICKER -> MyTimePicker(onTimeChange = { hour, min ->
                    selectedLocalDate = selectedLocalDate.withHour(hour).withMinute(min)
                    updateDateTime(selectedLocalDate)
                })
            }
        }
    }
}

fun dateFromPredefinedItems(item: EventInstantPickItems, previousValue: LocalDateTime): LocalDateTime = when(item){
        EventInstantPickItems.SELECTED_TODAY ->
            LocalDateTime.now().withHour(previousValue.hour).withMinute(previousValue.minute)
        EventInstantPickItems.SELECTED_TOMORROW ->
            LocalDateTime.now().withHour(previousValue.hour).withMinute(previousValue.minute).plusDays(1)
        EventInstantPickItems.SELECTED_MORNING ->
            previousValue.withHour(8).withMinute(0)
        EventInstantPickItems.SELECTED_EVENING ->
            previousValue.withHour(20).withMinute(0)
        else -> throw IllegalArgumentException("function only accepts predefined values")
}

@Composable
fun highlightIfSelected(item: EventInstantPickItems, selectedItem: EventInstantPickItems): Color {
    return if(item == selectedItem) MaterialTheme.colors.primary else Color.Unspecified
}

fun pickItemText(item: EventInstantPickItems) = when(item){
    EventInstantPickItems.SELECTED_TODAY -> "Today"
    EventInstantPickItems.SELECTED_TOMORROW -> "Tomorrow"
    EventInstantPickItems.SELECTED_PICK_DATE -> "Pick Date"
    EventInstantPickItems.SELECTED_MORNING -> "7 am"
    EventInstantPickItems.SELECTED_EVENING -> "8 pm"
    EventInstantPickItems.SELECTED_PICK_TIME -> "Pick Time"
}

fun pickItemIcon(item: EventInstantPickItems) = when(item){
    EventInstantPickItems.SELECTED_TODAY -> Icons.Filled.Today
    EventInstantPickItems.SELECTED_TOMORROW -> Icons.Filled.Event
    EventInstantPickItems.SELECTED_PICK_DATE -> Icons.Filled.DateRange
    EventInstantPickItems.SELECTED_MORNING -> Icons.Filled.LightMode
    EventInstantPickItems.SELECTED_EVENING -> Icons.Filled.DarkMode
    EventInstantPickItems.SELECTED_PICK_TIME -> Icons.Filled.Schedule
}


@Composable
fun PickItem(item: EventInstantPickItems, color: Color, onClick: (EventInstantPickItems) -> Unit){
    Row(modifier = Modifier
        .padding(8.dp)
        .clickable(true) { onClick(item) }
    ) {
        Icon(
            imageVector = pickItemIcon(item),
            contentDescription = "",
            modifier = Modifier.padding(end = 4.dp),
            tint = color
        )
        Text(
            text = pickItemText(item),
            style = TextStyle(color = color)
        )
    }
}

@Composable
fun DatePick(selectedItem: EventInstantPickItems, onClick: (EventInstantPickItems) -> Unit){
    Column{
        for(item in listOf(
            EventInstantPickItems.SELECTED_TODAY,
            EventInstantPickItems.SELECTED_TOMORROW,
            EventInstantPickItems.SELECTED_PICK_DATE)){
            PickItem(item = item, color = highlightIfSelected(item = item, selectedItem = selectedItem), onClick = onClick)
        }
    }
}

@Composable
fun TimePick(selectedItem: EventInstantPickItems, onClick: (EventInstantPickItems) -> Unit){
    Column {
        for(item in listOf(
            EventInstantPickItems.SELECTED_MORNING,
            EventInstantPickItems.SELECTED_EVENING,
            EventInstantPickItems.SELECTED_PICK_TIME,)){
            PickItem(item = item, color = highlightIfSelected(item = item, selectedItem = selectedItem), onClick = onClick)
        }
    }
}

@Composable
fun MyDatePicker(onDateChange: (Int, Int, Int) -> Unit){
    AndroidView(
        factory = { CalendarView(it) },
        update = { view ->
            view.setOnDateChangeListener { _, year, mon, dom ->
                onDateChange(year, mon, dom)
            }
        }
    )
}

@Composable
fun MyTimePicker(onTimeChange: (Int, Int) -> Unit = {_, _ ->}) {
    AndroidView(
        factory = { TimePicker(it) },
        update = { view ->
            view.setOnTimeChangedListener { _, hour, min ->
                onTimeChange(hour, min)
            }
        }
    )
}

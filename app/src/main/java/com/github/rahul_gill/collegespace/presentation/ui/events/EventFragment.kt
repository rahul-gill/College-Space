package com.github.rahul_gill.collegespace.presentation.ui.events

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.data.room.entities.PersonalEventEntity
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.presentation.ui.events.schedule.UiEvent
import com.github.rahul_gill.collegespace.presentation.ui.events.schedule.WeekSchedule
import com.github.rahul_gill.collegespace.presentation.viewmodels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EventFragment @Inject constructor(): Fragment(){
    private val args : EventFragmentArgs by navArgs()
    private val viewModel: EventViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupNotificationChannels(requireContext())
        args.argBundle?.let {
            Timber.d("In=> $it")
            Timber.d("Out=> ${PersonalEventEntity.from(it)}")
            viewModel.insertEvent(PersonalEventEntity.from(it))
            setupEventNotification(requireContext(),it, timeBefore = 10 * 1000)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                var eventInfoDialogShowing by remember { mutableStateOf(false) }
                var eventInfoData by remember { mutableStateOf(UiEvent()) }
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { goToNewEventScreen() },
                            content = {
                                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    content = {
                        val eventList by viewModel.eventList.observeAsState()
                        WeekSchedule(
                            events = eventList?.map{ UiEvent.from (it) } ?: listOf(),
                            onClick = {
                                eventInfoData = it
                                eventInfoDialogShowing = true
                            }
                        )
                        EventInfoDialog(
                            uiEvent = eventInfoData,
                            onEventEdit = { goToNewEventScreen(eventInfoData) },
                            onEventDelete = { viewModel.deleteEvent(eventInfoData.toPersonalEventEntity()) },
                            eventInfoDialogShowing,
                            onDismiss = { eventInfoDialogShowing = false }
                        )
                    }
                )
            }
        }
    }
    private fun goToNewEventScreen(uiEvent: UiEvent? = null){
        findNavController().navigate(EventFragmentDirections.actionEventFragmentToNewEventFragment())
    }

    private fun setupEventNotification(context: Context, event: Event, timeBefore: Long){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if(event.repeatPeriod == null){
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                event.start.time - timeBefore,
                getEventPendingIntent(event)
            )
        }
    }

    private fun getEventPendingIntent(event: Event) = PendingIntent.getBroadcast(
        requireContext(),
        1,
        Intent(requireContext(), EventReceiver::class.java).putExtra(EVENT_DATA, event.eventName),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun cancelEventNotification(context: Context, event: Event){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if(event.repeatPeriod == null){
           alarmManager.cancel(getEventPendingIntent(event))
        }
    }



    private fun setupNotificationChannels(context: Context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(eventNotificationChannel, eventNotificationChannel, importance)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
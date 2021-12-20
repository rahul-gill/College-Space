package com.artisticent.collegespace.presentation.ui.events

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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.artisticent.collegespace.R
import com.artisticent.collegespace.data.room.entities.PersonalEventEntity
import com.artisticent.collegespace.presentation.ui.events.schedule.UiEvent
import com.artisticent.collegespace.presentation.ui.events.schedule.WeekSchedule
import com.artisticent.collegespace.presentation.viewmodels.EventViewModel
import com.artisticent.collegespace.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject


@AndroidEntryPoint
class EventFragment @Inject constructor(): Fragment(){
    private val args : EventFragmentArgs by navArgs()
    private val viewModel: EventViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //get args from New Event Fragment
        val event = PersonalEventEntity()
        val bundle = args.argBundle
        if(bundle != null) {
            event.eventName = bundle.name
            event.start = Util.toDate(LocalDateTime.ofEpochSecond(bundle.startDate,0, ZoneOffset.UTC))
            event.end = Util.toDate(LocalDateTime.ofEpochSecond(bundle.endDate,0, ZoneOffset.UTC))
            viewModel.insertEvent(event)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold(
                    floatingActionButton = { FloatingActionButton(
                            onClick = {
                                findNavController().navigate(EventFragmentDirections.actionEventFragmentToNewEventFragment())
                            },
                            content = {
                                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    content = {
                        val eventList by viewModel.eventList.observeAsState()
                        WeekSchedule(
                            events = eventList?.map{ UiEvent.from (it) } ?: listOf()
                        )
                    }
                )
            }
        }
    }
}
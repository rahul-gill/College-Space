package com.github.rahul_gill.collegespace.presentation.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.rahul_gill.collegespace.domain.models.DurationWrapper
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.util.Util
import java.time.Duration
import java.time.LocalDateTime

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class NewEventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                NewEventScreen(onDone = onDone)
            }
        }
    }

    private val onDone = { name: String, start: LocalDateTime, end: LocalDateTime, description: String ->
        val event = Event(
            eventName = name,
            start = Util.toDate(start),
            duration = DurationWrapper.from(Duration.between(start, end)),
            description = description
        )
        findNavController().navigate(
            NewEventFragmentDirections.actionNewEventFragmentToEventFragment(event)
        )
    }
}



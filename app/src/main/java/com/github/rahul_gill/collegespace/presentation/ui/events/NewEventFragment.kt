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
import java.time.LocalDateTime
import java.time.ZoneOffset

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
        val argsBundle = EventArg(
            name = name,
            startDate = start.toEpochSecond(ZoneOffset.UTC),
            endDate = end.toEpochSecond(ZoneOffset.UTC),
            description = description
        )
        findNavController().navigate(
            NewEventFragmentDirections.actionNewEventFragmentToEventFragment(
                argsBundle
            )
        )
    }
}



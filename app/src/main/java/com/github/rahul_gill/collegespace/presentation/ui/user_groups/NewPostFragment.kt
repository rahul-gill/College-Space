package com.github.rahul_gill.collegespace.presentation.ui.user_groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rahul_gill.collegespace.domain.models.DurationWrapper
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.presentation.ui.events.NewEventScreen
import com.github.rahul_gill.collegespace.presentation.viewmodels.PostsViewModel
import com.github.rahul_gill.collegespace.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDateTime

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class NewPostFragment : Fragment() {
    private val args: NewPostFragmentArgs by navArgs()
    val viewModel: PostsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                NewEventScreen(
                    onDone = { name: String, start: LocalDateTime, end: LocalDateTime, description: String ->
                        viewModel.addPost(Event(
                            eventName = name,
                            start = Util.toDate(start),
                            duration = DurationWrapper.from(Duration.between(start, end)),
                            description = description,
                            userGroupName = args.userGroup
                        ))
                        findNavController().navigateUp()
                    }
                )
            }
        }
    }

}
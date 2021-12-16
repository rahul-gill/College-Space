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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EventFragment @Inject constructor(): Fragment(){
//    private lateinit var binding: FragmentEventBinding
    private val args : EventFragmentArgs by navArgs()
    private val viewModel: EventViewModel by viewModels()
//    private var dialog : AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //get args from New Event Fragment
        val event = PersonalEventEntity()
        val bundle = args.argBundle
        if(bundle != null) {
            event.eventName = bundle.name
            event.start = bundle.startDate.time
            event.end = bundle.endDate.time
//            event.startTime = bundle.startDate
//            event.endTime = bundle.endDate
//            event.color = when(bundle.type){
//                "Personal Event" ->  resources.getColor(R.color.colorPrimary,null)
//                "Group Event" ->  resources.getColor(R.color.colorSecondary,null)
//                else -> resources.getColor(R.color.background_gray,null)
//            }

            viewModel.insertEvent(event)
//                .also {
//                mWeekView.notifyDatasetChanged()
//            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            findNavController().navigate(EventFragmentDirections.actionEventFragmentToNewEventFragment())
                        }) { Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "") }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) {
                    val eventList by viewModel.eventList.observeAsState()
                    WeekSchedule(
                        events = eventList?.map{ UiEvent.from (it) } ?: listOf()
                    )
                }

            }
        }

//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false)
//
//        //things for WeekView
//        val mMonthChangeListener = MonthChangeListener { _, newMonth ->
//            return@MonthChangeListener viewModel.eventList.value?.filter {
//                it.startTime.get(Calendar.MONTH) == newMonth
//            }
//        }
//
//        val mEventClickListener = WeekView.EventClickListener { event, _ ->
//            val comp = event.startTime.timeInMillis - Calendar.getInstance().timeInMillis
//            if(comp > 0){
//                var minutes = comp / 60000
//                val hours = minutes / 60
//                minutes %= 60
//                Util.toast(requireContext(), "$hours:$minutes to go")
//            }
//        }
//
//        val mEventLongPressListener = WeekView.EventLongPressListener { event, _ ->
//            if (dialog == null) {
//                dialog = AlertDialog.Builder(this.activity)
//                    .setTitle("Delete this event?")
//                    .setMessage("Do you want to delete the event ${event.name}")
//                    .setPositiveButton("Yes") { _, _ ->
//                        viewModel.deleteEvent(event as EventModelOld)
//                    }.setNegativeButton("No") { _, _ ->
//                        Util.toast(requireContext(), "Deletion cancelled")
//                    }.create()
//            }
//            dialog!!.show()
//        }
//
//
//
//
//        //assigning weekView things
//        val mWeekView = binding.weekView
//        mWeekView.setOnEventClickListener(mEventClickListener)
//        mWeekView.eventLongPressListener = mEventLongPressListener
//        mWeekView.monthChangeListener = mMonthChangeListener
//        mWeekView.goToHour(8.0)
//        mWeekView.goToToday()
//
//        if(this.activity?.resources?.configuration?.orientation  == Configuration.ORIENTATION_LANDSCAPE){
//            mWeekView.numberOfVisibleDays = 7
//        }else{
//            mWeekView.numberOfVisibleDays = 5
//        }
//
//        mWeekView.dateTimeInterpreter = object : DateTimeInterpreter {
//            override fun interpretDate(date: Calendar?): String {
//                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
//                val weekday = weekdayNameFormat.format(date!!.time).uppercase()
//                val format = SimpleDateFormat("d/MM", Locale.getDefault())
//                return "$weekday ${format.format(date.time)}"
//            }
//
//            override fun interpretTime(hour: Int): String {
//                return when {
//                    hour == 0 -> "12:00 AM"
//                    hour < 12 -> String.format("%02d:00 AM", hour)
//                    hour == 12 -> "12:00 PM"
//                    else -> String.format("%02d:00 PM", hour % 12)
//
//                }
//
//            }
//        }


//        //navigate to New Event Fragment
//        binding.eventFab.setOnClickListener{
//            findNavController().
//            navigate(EventFragmentDirections.actionEventFragmentToNewEventFragment())
//        }



//        viewModel.eventList.observe(viewLifecycleOwner,{
//            mWeekView.notifyDatasetChanged()
//        })
//        viewModel.eventListUpdate.observe(viewLifecycleOwner,{
//            if(it == true){
//                mWeekView.notifyDatasetChanged()
//                viewModel.doneUpdating()
//            }
//        })
//
//        return binding.root
    }
}
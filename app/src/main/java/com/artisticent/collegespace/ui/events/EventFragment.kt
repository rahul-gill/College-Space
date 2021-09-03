package com.artisticent.collegespace.ui.events

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alamkanak.weekview.DateTimeInterpreter
import com.alamkanak.weekview.MonthLoader.MonthChangeListener
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentEventBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment @Inject constructor(): Fragment(){
    private lateinit var binding: FragmentEventBinding
    private val args : EventFragmentArgs by navArgs()
    private val viewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false)

        //things for WeekView
        viewModel.loadEvents()
        val mMonthChangeListener = MonthChangeListener{ _, month ->
            return@MonthChangeListener viewModel.eventList.value?.filter {
                it.startTime.get(Calendar.MONTH) == month
            }
        }
        val mEventClickListener = WeekView.EventClickListener { event, _ ->
            Toast.makeText(this.activity, "Clicked  ${event.name} ${viewModel.eventList.value!!.size}" , Toast.LENGTH_SHORT).show()
        }
        val mEventLongPressListener = WeekView.EventLongPressListener{ event, _ ->
            Toast.makeText(this.activity, "${event.name} deleted", Toast.LENGTH_SHORT).show()
        }

        //assigning weekView things
        val mWeekView = binding.weekView
        mWeekView.setOnEventClickListener(mEventClickListener)
        mWeekView.eventLongPressListener = mEventLongPressListener
        mWeekView.monthChangeListener = mMonthChangeListener
        mWeekView.goToHour(8.0)
        mWeekView.goToToday()

        if(this.activity?.resources?.configuration?.orientation  == Configuration.ORIENTATION_LANDSCAPE){
            mWeekView.numberOfVisibleDays = 7
        }else{
            mWeekView.numberOfVisibleDays = 5
        }

        mWeekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar?): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val weekday = weekdayNameFormat.format(date!!.time).uppercase()
                val format = SimpleDateFormat("d/MM", Locale.getDefault())
                return "$weekday ${format.format(date.time)}"
            }

            override fun interpretTime(hour: Int): String {
                return when {
                    hour == 0 -> "12:00 AM"
                    hour < 12 -> String.format("%02d:00 AM", hour)
                    hour == 12 -> "12:00 PM"
                    else -> String.format("%02d:00 PM", hour % 12)

                }

            }
        }


        //navigate to New Event Fragment
        binding.eventFab.setOnClickListener{
            findNavController().
            navigate(EventFragmentDirections.actionEventFragmentToNewEventFragment())
        }
        //get args from New Event Fragment
        val event = WeekViewEvent()
        val bundle = args.argBundle
        if(bundle != null) {
            event.name = bundle.name
            event.startTime = bundle.startDate
            event.endTime = bundle.endDate
            viewModel.insertEvent(event).also {
                mWeekView.notifyDatasetChanged()
            }
        }


        viewModel.eventList.observe(viewLifecycleOwner,{
            mWeekView.notifyDatasetChanged()
        })

        return binding.root
    }




}
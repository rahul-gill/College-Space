package com.artisticent.collegespace.ui.events

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alamkanak.weekview.DateTimeInterpreter
import com.alamkanak.weekview.MonthLoader.MonthChangeListener
import com.alamkanak.weekview.WeekView
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentEventBinding
import com.artisticent.collegespace.repository.models.EventModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment @Inject constructor(): Fragment(){
    private lateinit var binding: FragmentEventBinding
    private val args : EventFragmentArgs by navArgs()
    private val viewModel: EventViewModel by viewModels()
    private var dialog : AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false)

        //things for WeekView
        val mMonthChangeListener = MonthChangeListener{ _, month ->
            return@MonthChangeListener viewModel.eventList.value?.filter {
                it.startTime.get(Calendar.MONTH) == month
            }
        }

        val mEventClickListener = WeekView.EventClickListener { event, _ ->
            Toast.makeText(this.activity, "Clicked  ${event.name} ${viewModel.eventList.value!!.size}" , Toast.LENGTH_SHORT).show()
        }

        val mEventLongPressListener = WeekView.EventLongPressListener { event, _ ->
            if (dialog == null) {
                dialog = AlertDialog.Builder(this.activity)
                    .setTitle("Delete this event?")
                    .setMessage("Do you want to delete the event ${event.name}")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteEvent(event as EventModel)
                        Toast.makeText(this.activity, "${event.name} deleted", Toast.LENGTH_SHORT)
                            .show()
                    }.setNegativeButton("No") { _, _ ->
                        Toast.makeText(this.activity, "Deletion cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }.create()
            }
            dialog!!.show()
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
        val event = EventModel()
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
        viewModel.eventListUpdate.observe(viewLifecycleOwner,{
            if(it == true){
                mWeekView.notifyDatasetChanged()
                viewModel.doneUpdating()
            }
        })

        return binding.root
    }




}
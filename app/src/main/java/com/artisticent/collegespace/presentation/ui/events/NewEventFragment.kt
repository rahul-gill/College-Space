package com.artisticent.collegespace.presentation.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentNewEventBinding
import java.util.*

class NewEventFragment : Fragment() {
    private lateinit var binding: FragmentNewEventBinding
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()
    private lateinit var type: String
    private var eventTypes = arrayOf("Personal Event","Contest Event","Group Event")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_event,container,false)

        binding.startDate.setOnClickListener{ chooseEventDate(true) }
        binding.startTime.setOnClickListener{ chooseEventTime(true) }
        binding.endDate.setOnClickListener{ chooseEventDate(false) }
        binding.endTime.setOnClickListener{ chooseEventTime(false) }
        val arrayAdapter =  ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,eventTypes)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.eventTypeSpinner.adapter = arrayAdapter
        binding.eventTypeSpinner.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               type = eventTypes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                type = "Personal Event"
            }

        }

        binding.addEventButton.setOnClickListener {

            val argsBundle = EventArg(
                name = binding.eventName.text.toString(),
                startDate = startDate,
                endDate = endDate,
                type = type
            )
            findNavController().navigate(
                NewEventFragmentDirections.actionNewEventFragmentToEventFragment(
                    argsBundle
                )
            )
        }
        return binding.root
    }


    private fun chooseEventDate(start:Boolean){
        val date = if(start){startDate} else {endDate}
        val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                date.set(year,month, dayOfMonth)
        },date.get(Calendar.YEAR),date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }
    private  fun chooseEventTime(start:Boolean){
        val date = if(start){startDate} else {endDate}
        val timePicker = TimePickerDialog(requireContext(), { _, hour, minute ->
            date.set(Calendar.HOUR,hour)
            date.set(Calendar.MINUTE,minute)
        },date.get(Calendar.HOUR),date.get(Calendar.MINUTE),false)

        timePicker.show()
    }
}



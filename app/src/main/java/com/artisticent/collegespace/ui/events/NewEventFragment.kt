package com.artisticent.collegespace.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentNewEventBinding
import java.util.*

class NewEventFragment : Fragment() {
    private lateinit var binding: FragmentNewEventBinding
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_event,container,false)

        binding.startDate.setOnClickListener{ chooseEventDate(true) }
        binding.startTime.setOnClickListener{ chooseEventTime(true) }
        binding.endDate.setOnClickListener{ chooseEventDate(false) }
        binding.endTime.setOnClickListener{ chooseEventTime(false) }


        binding.addEventButton.setOnClickListener {
            val argsBundle = EventArg(
                name = binding.eventName.text.toString(),
                startDate = startDate,
                endDate = endDate
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
        val datePicker = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{  _,year,month,dayofMonth ->
                date.set(year,month, dayofMonth)
        },date.get(Calendar.YEAR),date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }
    private  fun chooseEventTime(start:Boolean){
        val date = if(start){startDate} else {endDate}
        val timePicker = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener{ _,hour,minute ->
            date.set(Calendar.HOUR,hour)
            date.set(Calendar.MINUTE,minute)
        },date.get(Calendar.HOUR),date.get(Calendar.MINUTE),false)

        timePicker.show()
    }
}



package com.artisticent.collegespace.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.domain.EventRepository
import com.artisticent.collegespace.domain.models.EventModelOld
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private var eventRepository: EventRepository) : ViewModel() {
    private val _eventList = MutableLiveData<MutableList<EventModelOld>>(mutableListOf())
    val eventList
        get() = _eventList
    private val _eventListUpdate = MutableLiveData(true)
    val eventListUpdate
        get() = _eventListUpdate
    fun doneUpdating() { _eventListUpdate.value = false }


    init{
        loadEvents()
    }

    private fun loadEvents(){
        var ret: MutableLiveData<List<EventModelOld>>
        viewModelScope.launch(Dispatchers.IO) {
            ret = MutableLiveData(eventRepository.loadAllEvents())
            this@EventViewModel._eventList.value!!.addAll(ret.value!!)
        }

    }
    fun insertEvent(event : EventModelOld){
        this@EventViewModel._eventList.value?.add(event)
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.insertEvent(event)
        }
    }
    fun deleteEvent(event: EventModelOld){
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.deleteEvent(event)
            withContext(Dispatchers.Main){
                _eventList.value?.remove(event)
                _eventListUpdate.value = true
            }
        }
    }


}
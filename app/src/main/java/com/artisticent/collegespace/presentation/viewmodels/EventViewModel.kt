package com.artisticent.collegespace.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.data.room.entities.PersonalEventEntity
import com.artisticent.collegespace.domain.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private var eventRepository: EventRepository) : ViewModel() {
//    private val _eventList = MutableLiveData<MutableList<EventModelOld>>(mutableListOf())
private val _eventList = MutableLiveData<MutableList<PersonalEventEntity>>(mutableListOf())
    val eventList
        get() = _eventList
    private val _eventListUpdate = MutableLiveData(true)
//    val eventListUpdate
//        get() = _eventListUpdate
//    fun doneUpdating() { _eventListUpdate.value = false }


    init{
        loadEvents()
    }

    private fun loadEvents(){
        var ret: MutableLiveData<List<PersonalEventEntity>>
        viewModelScope.launch(Dispatchers.Main) {
            ret = MutableLiveData(eventRepository.loadAllEvents())
            this@EventViewModel._eventList.value = ret.value!!.toMutableList()
        }

    }
    fun insertEvent(event : PersonalEventEntity){
        this@EventViewModel._eventList.value?.add(event)
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.insertEvent(event)
        }
    }
    fun deleteEvent(event: PersonalEventEntity){
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.deleteEvent(event)
            withContext(Dispatchers.Main){
                _eventList.value?.remove(event)
                _eventListUpdate.value = true
            }
        }
    }


}
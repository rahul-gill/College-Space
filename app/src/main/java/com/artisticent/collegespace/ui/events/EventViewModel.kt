package com.artisticent.collegespace.ui.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.repository.Repository
import com.artisticent.collegespace.repository.models.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(var repository: Repository) : ViewModel() {
    private val _eventList = MutableLiveData<MutableList<EventModel>>(mutableListOf())
    val eventList
        get() = _eventList
    private val _eventListUpdate = MutableLiveData(true)
    val eventListUpdate
        get() = _eventListUpdate
    fun doneUpdating() { _eventListUpdate.value = false }


    init{
        loadEvents()
    }

    fun loadEvents(){
        var ret: MutableLiveData<List<EventModel>>
        viewModelScope.launch(Dispatchers.IO) {
            ret = MutableLiveData(repository.loadAllEvents())
            this@EventViewModel._eventList.value!!.addAll(ret.value!!)
        }

    }
    fun insertEvent(event : EventModel){
        viewModelScope.launch(Dispatchers.IO) {
            this@EventViewModel._eventList.value?.add(event)
            repository.insertEvent(event)
        }
    }
    fun deleteEvent(event: EventModel){
        viewModelScope.launch(Dispatchers.IO) {
            _eventList.value?.remove(event)
            repository.deleteEvent(event)
            _eventListUpdate.value = false
        }
    }


}
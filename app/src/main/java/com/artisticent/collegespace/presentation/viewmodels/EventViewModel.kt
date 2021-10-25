package com.artisticent.collegespace.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.domain.Repository
import com.artisticent.collegespace.domain.models.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        this@EventViewModel._eventList.value?.add(event)
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertEvent(event)
        }
    }
    fun deleteEvent(event: EventModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent(event)
            withContext(Dispatchers.Main){
                _eventList.value?.remove(event)
                _eventListUpdate.value = true
            }
        }
    }


}
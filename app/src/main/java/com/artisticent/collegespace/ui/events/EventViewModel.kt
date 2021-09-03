package com.artisticent.collegespace.ui.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.repository.models.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(var repository: Repository) : ViewModel() {
    var eventList = MutableLiveData<MutableList<WeekViewEvent>>(mutableListOf())

    fun loadEvents(){
        var ret = MutableLiveData<List<WeekViewEvent>>(listOf())
        viewModelScope.launch {
            ret = withContext(Dispatchers.IO){
                MutableLiveData(repository.loadAllEvents())
            }
        }
        if(eventList.value == null){
            eventList.value = mutableListOf<WeekViewEvent>()
        }
        eventList.value!!.addAll(ret.value!!)
    }
    fun insertEvent(event : WeekViewEvent){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                eventList.value?.add(event)
                if(eventList.value == null){
                    eventList.value = mutableListOf(event)
                }
                repository.insertEvent(event)
            }
        }
    }

}
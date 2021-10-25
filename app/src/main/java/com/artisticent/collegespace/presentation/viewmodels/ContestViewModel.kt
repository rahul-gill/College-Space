package com.artisticent.collegespace.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContestViewModel @Inject constructor(val repository: Repository): ViewModel() {
    private var _contestList = MutableLiveData<List<ContestModel>>()
    val contestList : LiveData<List<ContestModel>>
        get() = _contestList


    private var _isLoadingEvents = MutableLiveData(false)
    val isLoadingEvents
        get() = _isLoadingEvents
    init{
        loadContestCached()
    }

    private fun loadContestCached(){
        viewModelScope.launch(Dispatchers.Main) {
            _isLoadingEvents.value = true
            _contestList.value = repository.loadContestDataFromCache()
            sortContestData()
            _isLoadingEvents.value = false
        }

    }

    fun loadContestDataNew(){
        try {


        viewModelScope.launch(Dispatchers.Main) {
            _isLoadingEvents.value = true
            _contestList.value = repository.loadContestDataFromNetwork()
            sortContestData()
            _isLoadingEvents.value = false
        }
        }catch (e: Exception){
            Timber.i("debug: ${e.message} in ContestViewModel")
        }
    }

    private fun sortContestData(){
        _contestList.value =  _contestList.value?.sortedWith { o1, o2 ->
            val res = o1!!.start_time.compareTo(o2!!.start_time)
            when {
                res > 0 -> -1
                res < 0 -> 1
                else -> 0
            }
        }
    }
}
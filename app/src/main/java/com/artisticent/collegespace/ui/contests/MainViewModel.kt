package com.artisticent.collegespace.ui.contests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.repository.models.ContestModel
import com.artisticent.collegespace.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: Repository): ViewModel() {
    private var _contestList = MutableLiveData<List<ContestModel>>()
    val contestList : LiveData<List<ContestModel>>
        get() = _contestList


    private var _doneLoading = MutableLiveData(true)
    val doneLoading
        get() = _doneLoading
    fun doneLoadingEventFinish(){ _doneLoading.value = false }


    init{
        loadContestCached()
    }

    private fun loadContestCached(){
        viewModelScope.launch(Dispatchers.IO) {
            _contestList = repository.loadContestDataFromCache()
            withContext(Dispatchers.Main) {
                _doneLoading.value = true
            }
        }
    }

    private fun loadContestDataNew(){
        viewModelScope.launch(Dispatchers.IO) {
            _contestList = repository.loadContestDataFromNetwork()
            withContext(Dispatchers.Main) {
                sortContestData()
                _doneLoading.value = true
            }
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
        _doneLoading.value = true
    }
}
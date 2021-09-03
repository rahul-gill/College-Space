package com.artisticent.collegespace.ui.contests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.repository.models.ContestModel
import com.artisticent.collegespace.repository.models.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var repository: Repository): ViewModel() {
    private var _contestList = MutableLiveData<List<ContestModel>>()
    val contestList : LiveData<List<ContestModel>>
        get() = _contestList

    private var _doneLoading = MutableLiveData<Boolean>(false)
    val doneLoading
        get() = _doneLoading


    fun loadContestCached(){
        viewModelScope.launch(Dispatchers.IO) {
            Timber.i("my Started Loading Data:")
            _contestList = repository.loadContestDataFromCache()
            withContext(Dispatchers.Main){
                _doneLoading.value = true
            }
            Timber.i("my End Loading Data: ${contestList.value?.size}")
        }
    }

    fun loadContestDataNew(){
        viewModelScope.launch(Dispatchers.IO) {
            Timber.i("my Started Loading Data")
            _contestList = repository.loadContestDataFromNetwork()
            Timber.i("my End Loading  ${_contestList.value?.size}")
        }
    }
}
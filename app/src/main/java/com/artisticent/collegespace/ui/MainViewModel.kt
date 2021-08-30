package com.artisticent.collegespace.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.remote.apis.CodeforcesApi
import com.artisticent.collegespace.remote.apis.Contest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    var contestList = MutableLiveData<List<Contest>>()
    @Inject lateinit var contestApi: CodeforcesApi

    fun onButtonClick(){
        viewModelScope.launch {
            Timber.i("Mydebug request start")
            val resultDeferred = contestApi.getContestListAsync()
            val result = resultDeferred.await()
            if(result.isSuccessful) {
                withContext(Dispatchers.Main) {
                    contestList.value = result.body()?.result
                    Timber.i("MyDebug ${contestList.value?.size.toString()}")
                }
            }else{
                contestList.value = listOf(Contest(
                    3600,
                    false,
                    1000,
                    "sample name",
                    "RUNNING",
                    0,
                    0L,
                    "CF"
                ))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("MyDebug viewmodel clear")
    }
}
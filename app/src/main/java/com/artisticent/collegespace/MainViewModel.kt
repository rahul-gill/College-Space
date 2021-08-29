package com.artisticent.collegespace

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.remote.apis.CodeforcesApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var contestApi: CodeforcesApi
    var textItemtext = MutableLiveData<String>()

    fun onButtonClick(){
        viewModelScope.launch {
            var getVal = contestApi.getContestList().await()

            textItemtext = MutableLiveData<String>(getVal.first)
        }
    }
}
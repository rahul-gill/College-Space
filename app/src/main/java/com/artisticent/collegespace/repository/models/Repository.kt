package com.artisticent.collegespace.repository.models

import androidx.lifecycle.MutableLiveData
import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.data.cf.CodeforcesApi
import com.artisticent.collegespace.data.cf.toContestModel
import com.artisticent.collegespace.data.room.ContestEntity
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.data.room.EventEntity
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {
    @Inject
    lateinit var databaseDao: EventDatabaseDao
    @Inject
    lateinit var contestApi: CodeforcesApi

    fun loadContestDataFromCache(): MutableLiveData<List<ContestModel>> {
        val ret = databaseDao.getAllContests().map {
            it.contest
        }
        return MutableLiveData(ret)
    }

    suspend fun loadContestDataFromNetwork(): MutableLiveData<List<ContestModel>> {
        val resultDeferred = contestApi.getContestListAsync()
        val result = resultDeferred.await()
        var contestList = if (result.isSuccessful) {
            result.body()?.result
                ?.filter { it.phase == "BEFORE" }
                ?.map { it.toContestModel() }!!
        } else {
            listOf()
        }
        for (contest in contestList) {
            Timber.i("my added ${contest.name}")
            databaseDao.insertContest(ContestEntity(eventId = contest.id, contest = contest))
        }

        Timber.i("MyDebug1 ${contestList.size}")
        return MutableLiveData(contestList)
    }

    suspend fun insertEvent(event: WeekViewEvent) {
        databaseDao.insertEvent(EventEntity(weekViewEvent = event))
    }

    suspend fun loadAllEvents(): List<WeekViewEvent> {
        return databaseDao.getAllEvents().map {
            it.weekViewEvent
        }
    }
}
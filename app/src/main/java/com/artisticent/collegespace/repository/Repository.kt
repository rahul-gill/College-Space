package com.artisticent.collegespace.repository

import androidx.lifecycle.MutableLiveData
import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.data.contestsApi.ContestApi
import com.artisticent.collegespace.data.room.ContestEntity
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.data.room.EventEntity
import com.artisticent.collegespace.repository.models.ContestModel
import com.artisticent.collegespace.repository.models.EventModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {
    @Inject lateinit var databaseDao: EventDatabaseDao
    @Inject lateinit var contestApi: ContestApi

    fun loadContestDataFromCache(): MutableLiveData<List<ContestModel>> {
        val ret = databaseDao.getAllContests().map {
            it.contest
        }
        return MutableLiveData(ret)
    }

    suspend fun loadContestDataFromNetwork(): MutableLiveData<List<ContestModel>> {
        val resultDeferred = contestApi.getAllContestsListAsync()
        val result = resultDeferred.await()

        val contestList = if (result.isSuccessful) {
            result.body()?.map{
                it.toContestModel()
            }
        } else {
            listOf()
        }
        for (contest in contestList!!) {
            databaseDao.insertContest(ContestEntity(
                eventId = contest.id,
                contest = contest))
        }
        return MutableLiveData(contestList)
    }

    suspend fun insertEvent(event: EventModel) {
        databaseDao.insertEvent(event.eventModelToEventEntity())
    }

    suspend fun deleteEvent(event : EventModel){
        databaseDao.deleteEvent(event.eventModelToEventEntity())
    }

    suspend fun loadAllEvents(): List<EventModel> {
        return databaseDao.getAllEvents().map {
            it.event
        }
    }
}
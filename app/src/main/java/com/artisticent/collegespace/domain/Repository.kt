package com.artisticent.collegespace.domain

import com.artisticent.collegespace.data.contestsApi.ContestApi
import com.artisticent.collegespace.data.contestsApi.ContestItem
import com.artisticent.collegespace.data.room.ContestEntity
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.models.EventModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {
    @Inject lateinit var databaseDao: EventDatabaseDao
    @Inject lateinit var contestApi: ContestApi

    suspend fun loadContestDataFromCache(): List<ContestModel> {
        return withContext(Dispatchers.IO) {
            databaseDao.getAllContests().map {
                it.contest
            }
        }
    }

    suspend fun loadContestDataFromNetwork(): List<ContestModel> {

        val resultDeferred = contestApi.getAllContestsListAsync()
        var resultNullable: retrofit2.Response<List<ContestItem>>? = null
        try {
            resultNullable = resultDeferred.await()
        }catch (e: Exception){
            Timber.i("debug: ${e.message}")
        }
        val result = resultNullable!!
        val contestList = if (result.isSuccessful) {
            result.body()?.map{
                it.toContestModel()
            }
        } else {
            listOf()
        }
        withContext(Dispatchers.IO) {
            for (contest in contestList!!) {
                databaseDao.insertContest(
                    ContestEntity(
                        eventId = contest.id,
                        contest = contest
                    )
                )
            }
        }

        return contestList!!
    }

    suspend fun insertEvent(event: EventModel) {
        withContext(Dispatchers.IO){
        databaseDao.insertEvent(event.eventModelToEventEntity())
        }
    }

    suspend fun deleteEvent(event : EventModel){
        withContext(Dispatchers.IO) {
            databaseDao.deleteEvent(event.eventModelToEventEntity())
        }
    }

    suspend fun loadAllEvents(): List<EventModel> {
        return withContext(Dispatchers.IO) {
            databaseDao.getAllEvents().map {
                it.event.id = it.eventId
                it.event
            }
        }
    }
}
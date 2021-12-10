package com.artisticent.collegespace.data

import com.artisticent.collegespace.data.contestsApi.ContestApi
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.data.room.entities.ContestEntity
import com.artisticent.collegespace.domain.EventRepository
import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.models.EventModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor() : EventRepository{
    @Inject
    lateinit var databaseDao: EventDatabaseDao
    @Inject
    lateinit var contestApi: ContestApi

    override suspend fun loadContestDataFromCache(): List<ContestModel> {
        return withContext(Dispatchers.IO) {
            databaseDao.getAllContests().map {
                it.contest
            }
        }
    }

    override suspend fun loadContestDataFromNetwork(): List<ContestModel> {
        val contestList: List<ContestModel>?
        withContext(Dispatchers.IO) {
            val result = contestApi.getAllContestsList()
            contestList = if (result.isSuccessful) {
                result.body()?.map {
                    it.toContestModel()
                }
            } else {
                listOf()
            }
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

    override suspend fun insertEvent(event: EventModel) {
        withContext(Dispatchers.IO){
            databaseDao.insertEvent(event.eventModelToEventEntity())
        }
    }

    override suspend fun deleteEvent(event : EventModel){
        withContext(Dispatchers.IO) {
            databaseDao.deleteEvent(event.eventModelToEventEntity())
        }
    }

    override suspend fun loadAllEvents(): List<EventModel> {
        return withContext(Dispatchers.IO) {
            databaseDao.getAllEvents().map {
                it.event.id = it.eventId
                it.event
            }
        }
    }
}
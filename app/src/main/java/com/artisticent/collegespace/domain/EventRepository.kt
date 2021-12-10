package com.artisticent.collegespace.domain

import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.models.EventModel
import javax.inject.Singleton

@Singleton
interface EventRepository {
    suspend fun loadContestDataFromCache(): List<ContestModel>
    suspend fun loadContestDataFromNetwork(): List<ContestModel>
    suspend fun insertEvent(event: EventModel)
    suspend fun deleteEvent(event : EventModel)
    suspend fun loadAllEvents(): List<EventModel>
}
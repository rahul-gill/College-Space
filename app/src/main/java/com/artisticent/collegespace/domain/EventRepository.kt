package com.artisticent.collegespace.domain

import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.models.EventModelOld
import javax.inject.Singleton

@Singleton
interface EventRepository {
    suspend fun loadContestDataFromCache(): List<ContestModel>
    suspend fun loadContestDataFromNetwork(): List<ContestModel>
    suspend fun insertEvent(event: EventModelOld)
    suspend fun deleteEvent(event : EventModelOld)
    suspend fun loadAllEvents(): List<EventModelOld>
}
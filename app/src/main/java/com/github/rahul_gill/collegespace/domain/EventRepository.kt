package com.github.rahul_gill.collegespace.domain

import com.github.rahul_gill.collegespace.data.room.entities.PersonalEventEntity
import com.github.rahul_gill.collegespace.domain.models.ContestModel
import javax.inject.Singleton

@Singleton
interface EventRepository {
    suspend fun loadContestDataFromCache(): List<ContestModel>
    suspend fun loadContestDataFromNetwork(): List<ContestModel>
    suspend fun insertEvent(event: PersonalEventEntity)
    suspend fun deleteEvent(event : PersonalEventEntity)
    suspend fun loadAllEvents(): List<PersonalEventEntity>
}
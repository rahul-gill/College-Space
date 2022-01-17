package com.github.rahul_gill.collegespace.data.room

import androidx.room.*
import com.github.rahul_gill.collegespace.data.room.entities.ContestEntity
import com.github.rahul_gill.collegespace.data.room.entities.PersonalEventEntity

@Dao
interface EventDatabaseDao {
    @Insert
    fun insertEvent(vararg event : PersonalEventEntity)

    @Update
    fun updateEvent(event: PersonalEventEntity)

    @Delete
    fun deleteEvent(event: PersonalEventEntity)

    @Query("SELECT * FROM PersonalEventEntity")
    fun getAllEvents() : List<PersonalEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContest(contest: ContestEntity)

    @Query("SELECT * FROM ContestEntity")
    fun getAllContests() : List<ContestEntity>
}
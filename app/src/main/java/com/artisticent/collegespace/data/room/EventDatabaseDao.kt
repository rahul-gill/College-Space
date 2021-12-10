package com.artisticent.collegespace.data.room

import androidx.room.*
import com.artisticent.collegespace.data.room.entities.ContestEntity
import com.artisticent.collegespace.data.room.entities.EventEntity

@Dao
interface EventDatabaseDao {
    @Insert
    fun insertEvent(event : EventEntity)

    @Update
    fun updateEvent(event: EventEntity)

    @Delete
    fun deleteEvent(event: EventEntity)

    @Query("SELECT * FROM EventEntity")
    fun getAllEvents() : List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContest(contest: ContestEntity)

    @Query("SELECT * FROM ContestEntity")
    fun getAllContests() : List<ContestEntity>
}
package com.artisticent.collegespace.data.room

import androidx.room.*

@Dao
interface EventDatabaseDao {
    @Insert
    fun insertEvent(event : EventEntity)

    @Update
    fun updateEvent(event: EventEntity)

    @Delete
    fun deleteEvent(event: EventEntity)

    @Query("SELECT * FROM evententity")
    fun getAllEvents() : List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContest(contest: ContestEntity)

    @Query("SELECT * FROM contestentity")
    fun getAllContests() : List<ContestEntity>
}
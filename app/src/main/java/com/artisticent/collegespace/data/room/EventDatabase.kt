package com.artisticent.collegespace.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [EventEntity::class, ContestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(WeekViewEventConverter::class)
abstract class  EventDatabase: RoomDatabase(){
    abstract val eventDatabaseDao: EventDatabaseDao
}
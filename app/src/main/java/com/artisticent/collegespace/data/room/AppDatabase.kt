package com.artisticent.collegespace.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.artisticent.collegespace.data.room.entities.*
import com.artisticent.collegespace.domain.models.Event

@Database(
    entities = [ContestEntity::class, PersonalEventEntity::class], //EventEntity::class
    version = 1,
    exportSchema = false
)
@TypeConverters(ModelJsonConverter::class)
abstract class  AppDatabase: RoomDatabase(){
    abstract val eventDatabaseDao: EventDatabaseDao
}
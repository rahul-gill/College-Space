package com.artisticent.collegespace.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.artisticent.collegespace.data.room.entities.EventEntity
import com.artisticent.collegespace.data.room.entities.ContestEntity
import com.artisticent.collegespace.data.room.entities.ModelJsonConverter

@Database(
    entities = [EventEntity::class, ContestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ModelJsonConverter::class)
abstract class  AppDatabase: RoomDatabase(){
    abstract val eventDatabaseDao: EventDatabaseDao
}
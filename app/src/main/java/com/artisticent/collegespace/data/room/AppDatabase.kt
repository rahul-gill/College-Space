package com.artisticent.collegespace.data.room

import android.graphics.ColorSpace
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [EventEntity::class, ContestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ModelJsonConvertor::class)
abstract class  AppDatabase: RoomDatabase(){
    abstract val eventDatabaseDao: EventDatabaseDao
    abstract val firebaseDao: FirebaseDao
}
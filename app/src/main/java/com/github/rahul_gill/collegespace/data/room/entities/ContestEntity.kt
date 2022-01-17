package com.github.rahul_gill.collegespace.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.rahul_gill.collegespace.domain.models.ContestModel


@Entity
data class ContestEntity(
    @PrimaryKey(autoGenerate = false)
    val eventId: Int = 0,
    @ColumnInfo(name = "contest")
    val contest: ContestModel
)
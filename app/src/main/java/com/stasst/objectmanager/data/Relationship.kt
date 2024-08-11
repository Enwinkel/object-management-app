package com.stasst.objectmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "relationships")
data class Relationship(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val parentObjectId: Long,
    val childObjectId: Long
)
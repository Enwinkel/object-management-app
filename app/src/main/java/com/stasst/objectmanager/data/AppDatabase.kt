package com.stasst.objectmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ObjectItem::class, Relationship::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun objectItemDao(): ObjectItemDao
    abstract fun relationshipDao(): RelationshipDao
}
package com.stasst.objectmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectItemDao {
    @Query("SELECT * FROM object_items")
    fun getAllObjects(): Flow<List<ObjectItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(item: ObjectItem): Long

    @Update
    suspend fun updateObject(item: ObjectItem)

    @Delete
    suspend fun deleteObject(item: ObjectItem)

    @Query("Delete FROM object_items WHERE id = :objectId")
    suspend fun deleteObjectById(objectId: Long)

    @Query("SELECT * FROM object_items WHERE id = :objectId")
    suspend fun getObjectById(objectId: Long): ObjectItem?

    @Query("SELECT * FROM object_items WHERE id NOT IN (:excludedIds)")
    suspend fun getObjectsExcludingIds(excludedIds: List<Long>): List<ObjectItem>

    @Query("""
        SELECT * FROM object_items 
        WHERE id IN (
            SELECT childObjectId FROM relationships WHERE parentObjectId = :parentId
            UNION
            SELECT parentObjectId FROM relationships WHERE childObjectId = :parentId
        )
    """)
    fun getRelatedObjects(parentId: Long): Flow<List<ObjectItem>>
}
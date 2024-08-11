package com.stasst.objectmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RelationshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationship(relationship: Relationship)

    @Delete
    suspend fun deleteRelationship(relationship: Relationship)

    @Query("""
        DELETE FROM relationships 
        WHERE (parentObjectId = :parentId AND childObjectId = :childId) 
        OR (parentObjectId = :childId AND childObjectId = :parentId)
    """)
    suspend fun deleteRelationshipByIds(parentId: Long, childId: Long)

    @Query("""
        SELECT childObjectId 
        FROM relationships 
        WHERE parentObjectId = :objectId
        UNION
        SELECT parentObjectId 
        FROM relationships 
        WHERE childObjectId = :objectId
    """)
    suspend fun getRelatedObjectIds(objectId: Long): List<Long>
}
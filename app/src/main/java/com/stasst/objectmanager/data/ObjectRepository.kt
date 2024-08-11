package com.stasst.objectmanager.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObjectRepository @Inject constructor(
    private val objectDao: ObjectItemDao,
    private val relationshipDao: RelationshipDao
) {
    fun getAllObjects(): Flow<List<ObjectItem>> = objectDao.getAllObjects()

    suspend fun insertObject(item: ObjectItem): Long = objectDao.insertObject(item)

    suspend fun updateObject(item: ObjectItem) = objectDao.updateObject(item)

    suspend fun deleteObjectById(id: Long) = objectDao.deleteObjectById(id)

    suspend fun getObjectById(id: Long): ObjectItem? = objectDao.getObjectById(id)

    fun getRelatedObjects(parentId: Long): Flow<List<ObjectItem>> = objectDao.getRelatedObjects(parentId)

    suspend fun insertRelationship(relationship: Relationship) = relationshipDao.insertRelationship(relationship)

    suspend fun deleteRelationshipByIds(parentId: Long, childId: Long) = relationshipDao.deleteRelationshipByIds(parentId, childId)

    suspend fun getObjectsExcludingCurrentAndRelated(currentObjectId: Long): List<ObjectItem> {
        val relatedIds = relationshipDao.getRelatedObjectIds(currentObjectId)
        val excludedIds = relatedIds.toMutableList()
        excludedIds.add(currentObjectId)
        return objectDao.getObjectsExcludingIds(excludedIds)
    }
}

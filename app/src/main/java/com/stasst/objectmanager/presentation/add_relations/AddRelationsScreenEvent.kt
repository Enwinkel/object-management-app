package com.stasst.objectmanager.presentation.add_relations

import androidx.compose.runtime.snapshots.SnapshotStateMap

sealed class AddRelationsScreenEvent {
    data class GetRelationsObjectsEvent(val id: Long) : AddRelationsScreenEvent()
    data class SaveRelationsEvent(
        val parentId: Long,
        val relatedObjects: SnapshotStateMap<Long, Boolean>,
    ) : AddRelationsScreenEvent()
}
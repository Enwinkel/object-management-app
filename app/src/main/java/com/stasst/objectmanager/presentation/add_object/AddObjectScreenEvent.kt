package com.stasst.objectmanager.presentation.add_object

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.stasst.objectmanager.data.ObjectItem

sealed class AddObjectScreenEvent {
    data class InsertObjectEvent(
        val newObject: ObjectItem,
        val relatedObjects: SnapshotStateMap<Long, Boolean>,
    ) : AddObjectScreenEvent()
}
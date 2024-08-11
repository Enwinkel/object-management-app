package com.stasst.objectmanager.presentation.edit_object

import com.stasst.objectmanager.data.ObjectItem

sealed class EditObjectScreenEvent {
    data class GetObjectEvent(val id: Long): EditObjectScreenEvent()
    data class UpdateRelationsEvent(val id: Long): EditObjectScreenEvent()
    data class UpdateObjectEvent(val objectItem: ObjectItem): EditObjectScreenEvent()
    data class DeleteRelationshipEvent(val parentId: Long, val childId: Long): EditObjectScreenEvent()
    data class DeleteObjectEvent(val id: Long): EditObjectScreenEvent()
}
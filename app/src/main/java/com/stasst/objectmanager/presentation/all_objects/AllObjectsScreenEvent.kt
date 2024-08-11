package com.stasst.objectmanager.presentation.all_objects

sealed class AllObjectsScreenEvent {
    data class DeleteObjectEvent(val id: Long): AllObjectsScreenEvent()
}
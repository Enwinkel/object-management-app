package com.stasst.objectmanager.presentation.edit_object

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stasst.objectmanager.data.ObjectItem
import com.stasst.objectmanager.data.ObjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditObjectViewModel @Inject constructor(
    val repository: ObjectRepository,
) : ViewModel() {
    private var _objectItem = MutableStateFlow<ObjectItem?>(null)
    val objectItem = _objectItem.asStateFlow()

    private var _relationsObjects = MutableStateFlow<List<ObjectItem>>(listOf())
    val relationsObjects = _relationsObjects.asStateFlow()

    fun onEvent(event: EditObjectScreenEvent) {
        when (event) {
            is EditObjectScreenEvent.GetObjectEvent -> {
                viewModelScope.launch {
                    _objectItem.value = repository.getObjectById(event.id)
                    repository.getRelatedObjects(event.id)
                        .collect { list ->
                            _relationsObjects.value = list
                        }
                }
            }

            is EditObjectScreenEvent.UpdateRelationsEvent -> {
                viewModelScope.launch {
                    _relationsObjects.value = repository.getRelatedObjects(event.id).first()
                }
            }

            is EditObjectScreenEvent.UpdateObjectEvent -> {
                viewModelScope.launch {
                    repository.updateObject(event.objectItem)
                }
            }

            is EditObjectScreenEvent.DeleteRelationshipEvent -> {
                viewModelScope.launch {
                    repository.deleteRelationshipByIds(event.parentId, event.childId)
                }
            }

            is EditObjectScreenEvent.DeleteObjectEvent -> {
                viewModelScope.launch {
                    repository.deleteObjectById(event.id)
                }
            }
        }
    }
}
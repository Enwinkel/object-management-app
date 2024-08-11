package com.stasst.objectmanager.presentation.add_relations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stasst.objectmanager.data.ObjectItem
import com.stasst.objectmanager.data.ObjectRepository
import com.stasst.objectmanager.data.Relationship
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRelationsViewModel @Inject constructor(
    val repository: ObjectRepository,
) : ViewModel() {

    private var _relationsObjects = MutableStateFlow<List<ObjectItem>>(listOf())
    val relationsObjects = _relationsObjects.asStateFlow()

    private val _saveStatus = MutableStateFlow<Boolean>(false)
    val saveStatus = _saveStatus.asStateFlow()

    fun onEvent(event: AddRelationsScreenEvent) {
        when (event) {
            is AddRelationsScreenEvent.GetRelationsObjectsEvent -> {
                viewModelScope.launch {
                    _relationsObjects.value =
                        repository.getObjectsExcludingCurrentAndRelated(event.id)
                }
            }

            is AddRelationsScreenEvent.SaveRelationsEvent -> {
                viewModelScope.launch {
                    event.relatedObjects.forEach { (childId, isSelected) ->
                        if (isSelected) {
                            repository.insertRelationship(
                                Relationship(
                                    parentObjectId = event.parentId,
                                    childObjectId = childId
                                )
                            )
                        }
                    }
                    _saveStatus.value = true
                }
            }
        }
    }
}
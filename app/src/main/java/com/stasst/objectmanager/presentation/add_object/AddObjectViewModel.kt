package com.stasst.objectmanager.presentation.add_object

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stasst.objectmanager.data.ObjectItem
import com.stasst.objectmanager.data.ObjectRepository
import com.stasst.objectmanager.data.Relationship
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddObjectViewModel @Inject constructor(
    private val repository: ObjectRepository,
) : ViewModel() {

    private var _relationsObjects = MutableStateFlow<List<ObjectItem>>(listOf())
    val relationsObjects = _relationsObjects.asStateFlow()

    private val _saveStatus = MutableStateFlow<Boolean>(false)
    val saveStatus = _saveStatus.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllObjects()
                .collectLatest { objects ->
                    _relationsObjects.value = objects
                }
        }
    }

    fun onEvent(event: AddObjectScreenEvent) {
        when (event) {
            is AddObjectScreenEvent.InsertObjectEvent -> {
                viewModelScope.launch {
                    val newObjectId = repository.insertObject(event.newObject)

                    if(event.relatedObjects.isEmpty()){
                        _saveStatus.value = true
                    } else{
                        event.relatedObjects.forEach { (id, isSelected) ->
                            if (isSelected) {
                                repository.insertRelationship(
                                    Relationship(
                                        parentObjectId = newObjectId,
                                        childObjectId = id
                                    )
                                )
                            }
                            _saveStatus.value = true
                        }
                    }
                }
            }
        }
    }
}
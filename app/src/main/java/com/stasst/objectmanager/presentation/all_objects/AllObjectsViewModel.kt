package com.stasst.objectmanager.presentation.all_objects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stasst.objectmanager.data.ObjectItem
import com.stasst.objectmanager.data.ObjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllObjectsViewModel @Inject constructor(
    val repository: ObjectRepository,
) : ViewModel() {

    private var _allObjects = MutableStateFlow<List<ObjectItem>>(listOf())
    val allObjects = _allObjects.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllObjects()
                .collectLatest { objects ->
                    _allObjects.value = objects
                }
        }
    }

    fun onEvent(event: AllObjectsScreenEvent) {
        when (event) {
            is AllObjectsScreenEvent.DeleteObjectEvent -> {
                viewModelScope.launch {
                    repository.deleteObjectById(event.id)
                }
            }
        }
    }
}
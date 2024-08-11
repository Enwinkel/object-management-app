package com.stasst.objectmanager.presentation.edit_object

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.stasst.objectmanager.data.ObjectItem
import kotlinx.coroutines.launch

@Composable
fun EditObjectScreen(
    navController: NavController,
    id: Long,
    viewModel: EditObjectViewModel = hiltViewModel(),
) {
    val relationsObjects by viewModel.relationsObjects.collectAsState()
    val objectItem by viewModel.objectItem.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var typeError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = id) {
        viewModel.onEvent(EditObjectScreenEvent.GetObjectEvent(id))
    }

    LaunchedEffect(key1 = objectItem) {
        objectItem?.let { item ->
            name = item.name
            description = item.description
            type = item.type
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                IconButton(onClick = {
                    val currentBackStackEntry = navController.currentBackStackEntry
                    if (currentBackStackEntry != null && currentBackStackEntry.getLifecycle().currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = "Edit object",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(
                        onClick = {
                            val currentBackStackEntry = navController.currentBackStackEntry
                            if (currentBackStackEntry != null && currentBackStackEntry.getLifecycle().currentState == Lifecycle.State.RESUMED) {
                                navController.popBackStack("objectList", inclusive = false)
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Home, contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(
                        onClick = {
                            showDialog = true
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                // Закрыть диалог без удаления
                                showDialog = false
                            },
                            title = {
                                Text(text = "Confirmation")
                            },
                            text = {
                                Text(text = "Are you sure you want to delete this object?")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.onEvent(EditObjectScreenEvent.DeleteObjectEvent(id = id))
                                        val currentBackStackEntry =
                                            navController.currentBackStackEntry
                                        if (currentBackStackEntry != null && currentBackStackEntry.getLifecycle().currentState == Lifecycle.State.RESUMED) {
                                            navController.popBackStack()
                                        }
                                        showDialog = false
                                    }
                                ) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                objectItem?.let { item ->
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (nameError) nameError = false
                        },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        isError = nameError,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (nameError) Color.Red else MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = if (nameError) Color.Red else MaterialTheme.colorScheme.background
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            if (descriptionError) descriptionError = false
                        },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        isError = descriptionError,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (descriptionError) Color.Red else MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = if (descriptionError) Color.Red else MaterialTheme.colorScheme.background
                        )

                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = type,
                        onValueChange = {
                            type = it
                            if (typeError) typeError = false
                        },
                        label = { Text("Type") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        isError = typeError,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (typeError) Color.Red else MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = if (typeError) Color.Red else MaterialTheme.colorScheme.background
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            var hasError = false

                            if (name.isBlank()) {
                                nameError = true
                                hasError = true
                            }

                            if (description.isBlank()) {
                                descriptionError = true
                                hasError = true
                            }

                            if (type.isBlank()) {
                                typeError = true
                                hasError = true
                            }

                            if (!hasError) {
                                val updateObject = ObjectItem(
                                    id = item.id,
                                    name = name,
                                    description = description,
                                    type = type
                                )
                                viewModel.onEvent(
                                    EditObjectScreenEvent.UpdateObjectEvent(
                                        updateObject
                                    )
                                )
                                coroutineScope.launch {
                                    if (snackbarHostState.currentSnackbarData == null) {
                                        snackbarHostState.showSnackbar(
                                            message = "Object updated",
                                            duration = SnackbarDuration.Short
                                        )
                                    } else {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar(
                                            message = "Object updated",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Save")
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Rrelations:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                objectItem?.let { item ->
                    IconButton(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            val currentBackStackEntry = navController.currentBackStackEntry
                            if (currentBackStackEntry != null && currentBackStackEntry.getLifecycle().currentState == Lifecycle.State.RESUMED) {
                                navController.navigate("addRelations/" + item.id)
                            }
                        }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            }

            if(relationsObjects.isEmpty()){
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No relations",
                        fontSize = 20.sp
                    )
                }
            }

            if(relationsObjects.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(relationsObjects) { _, objectItem ->
                        var isMenuVisible by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                                .clickable {
                                    navController.navigate("objectDetail/" + objectItem.id)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = objectItem.type + ": " + objectItem.name,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = objectItem.description)
                                }
                                Box {
                                    IconButton(onClick = {
                                        isMenuVisible = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "More options"
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = isMenuVisible,
                                        onDismissRequest = { isMenuVisible = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "Edit",
                                                    fontSize = 14.sp
                                                )
                                            },
                                            onClick = {
                                                navController.navigate("objectDetail/" + objectItem.id)
                                                isMenuVisible = false
                                            }
                                        )

                                        HorizontalDivider(
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "Delete relation",
                                                    fontSize = 14.sp
                                                )
                                            },
                                            onClick = {
                                                viewModel.onEvent(
                                                    EditObjectScreenEvent
                                                        .DeleteRelationshipEvent(id, objectItem.id)
                                                )
                                                isMenuVisible = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState
        )
    }
}
package com.stasst.objectmanager.presentation.add_object

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.stasst.objectmanager.data.ObjectItem

@Composable
fun AddObjectScreen(navController: NavController, viewModel: AddObjectViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var typeError by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateMapOf<Long, Boolean>() }

    val relationsObjects by viewModel.relationsObjects.collectAsState()
    val saveStatus by viewModel.saveStatus.collectAsState()

    LaunchedEffect(saveStatus) {
        if (saveStatus) {
            val currentBackStackEntry = navController.currentBackStackEntry
            if (currentBackStackEntry != null && currentBackStackEntry.getLifecycle().currentState == Lifecycle.State.RESUMED) {
                navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
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
                    text = "Create new object",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (nameError) nameError = false
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
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
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
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
                        val newObject =
                            ObjectItem(name = name, description = description, type = type)
                        viewModel.onEvent(
                            AddObjectScreenEvent.InsertObjectEvent(
                                newObject,
                                selectedItems
                            )
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Create")
            }
        }

        Spacer(Modifier.padding(8.dp))

        Text(
            modifier = Modifier.padding(8.dp),
            text = "Add relations:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if(relationsObjects.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No relations available",
                    fontSize = 20.sp
                )
            }
        }

        if (relationsObjects.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(relationsObjects) { _, objectItem ->
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
                            Checkbox(
                                checked = selectedItems[objectItem.id] ?: false,
                                onCheckedChange = { isChecked ->
                                    selectedItems[objectItem.id] = isChecked
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
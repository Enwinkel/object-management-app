package com.stasst.objectmanager.presentation.all_objects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AllObjectsScreen(
    navController: NavController,
    viewModel: AllObjectsViewModel = hiltViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val allObjects by viewModel.allObjects.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth().background(MaterialTheme.colorScheme.primary),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Objects",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            TextField(
                value = searchQuery,
                onValueChange = { query -> searchQuery = query },
                placeholder = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )

            if(allObjects.isEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Press the button below to add an object",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


            if(allObjects.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 75.dp)
                ) {
                    val filteredItems = allObjects.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.description.contains(searchQuery, ignoreCase = true) ||
                                it.type.contains(searchQuery, ignoreCase = true)
                    }

                    itemsIndexed(filteredItems) { _, objectItem ->
                        var isMenuVisible by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
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
                                                    text = "Delete",
                                                    fontSize = 14.sp
                                                )
                                            },
                                            onClick = {
                                                viewModel
                                                    .onEvent(
                                                        AllObjectsScreenEvent
                                                            .DeleteObjectEvent(id = objectItem.id)
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

        FloatingActionButton(
            onClick = {
                navController.navigate("addObject")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
}
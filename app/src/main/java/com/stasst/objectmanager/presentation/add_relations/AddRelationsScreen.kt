package com.stasst.objectmanager.presentation.add_relations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

@Composable
fun AddRelationsScreen(
    navController: NavController,
    viewModel: AddRelationsViewModel = hiltViewModel(),
    id: Long,
) {
    val relationsObjects by viewModel.relationsObjects.collectAsState()
    val saveStatus by viewModel.saveStatus.collectAsState()
    val selectedItems = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(key1 = id) {
        viewModel.onEvent(AddRelationsScreenEvent.GetRelationsObjectsEvent(id))
    }

    LaunchedEffect(saveStatus) {
        if (saveStatus) {
            val currentBackStackEntry = navController.currentBackStackEntry
            if (currentBackStackEntry != null && currentBackStackEntry.getLifecycle().currentState == Lifecycle.State.RESUMED) {
                navController.popBackStack()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
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
                    text = "Add relations",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(relationsObjects) { _, objectItem ->
                Card(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
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

        if (relationsObjects.isEmpty()) {
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

        Spacer(modifier = Modifier.weight(1f))

        if (relationsObjects.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        viewModel.onEvent(
                            AddRelationsScreenEvent.SaveRelationsEvent(
                                id,
                                selectedItems
                            )
                        )
                    }
                ) {
                    Text(text = "Add relations")
                }
            }
        }
    }
}
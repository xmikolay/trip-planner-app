package com.example.tripplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripplanner.data.Trip
import com.example.tripplanner.ui.viewmodel.TripViewModel

//screen for adding a trip
//has a form with fields for trip name, location and dates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(
    onNavigateBack: () -> Unit,
    onTripAdded: () -> Unit,
    viewModel: TripViewModel = viewModel(factory = TripViewModel.Factory)
) {
    //form state
    //creates state variables to empty strings for each field
    var tripName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    //validation errors
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Trip") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            //trip name field
            OutlinedTextField(
                value = tripName,
                onValueChange = {
                    tripName = it
                    showError = false
                },
                label = { Text("Trip Name") },
                placeholder = { Text("e.g. Vacation to Paris") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && tripName.isBlank()
            )

            //location field
            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                    showError = false
                },
                label = { Text("Location") },
                placeholder = {Text("e.g. Paris, France")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && location.isBlank()
            )

            //start date field
            OutlinedTextField(
                value = startDate,
                onValueChange = {
                    startDate = it
                    showError = false
                },
                label = { Text("Start Date") },
                placeholder = { Text("e.g., October 26th") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && startDate.isBlank()
            )

            //end date field
            OutlinedTextField(
                value = endDate,
                onValueChange = {
                    endDate = it
                    showError = false
                },
                label = { Text("End Date") },
                placeholder = { Text("e.g., October 31st") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && endDate.isBlank()
            )

            //error message
            if (showError){
                Text(
                    text = "Please fill in all fields",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //save button
            Button(
                onClick = {
                    if (tripName.isBlank() || location.isBlank() || startDate.isBlank() || endDate.isBlank()){
                        showError = true
                    } else {
                        val newTrip = Trip(
                            name = tripName.trim(),
                            location = location.trim(),
                            startDate = startDate.trim(),
                            endDate = endDate.trim()
                        )
                        viewModel.addTrip(newTrip)
                        onTripAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save Trip")
            }
        }
    }
}
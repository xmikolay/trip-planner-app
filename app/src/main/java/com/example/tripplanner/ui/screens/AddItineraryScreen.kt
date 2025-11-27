package com.example.tripplanner.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripplanner.data.ItineraryItem
import com.example.tripplanner.ui.viewmodel.TripViewModel
import java.text.SimpleDateFormat
import java.util.*

//screen for adding a new itinerary item to a trip
//has a form with fields for activity details

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItineraryScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    onItemAdded: () -> Unit,
    viewModel: TripViewModel = viewModel(factory = TripViewModel.Factory)
){
    //load trip data
    LaunchedEffect(tripId) {
        viewModel.loadItineraryItems(tripId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val currentTrip = uiState.currentTrip

    //find trip if not already selected
    LaunchedEffect(tripId, uiState.tripList) {
        if (currentTrip == null || currentTrip.id != tripId) {
            val trip = uiState.tripList.find {it.id == tripId}
            trip?.let {viewModel.selectTrip(it)}
        }
    }

    //form state
    var itemName by remember {mutableStateOf("")}
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("")  }
    var notes by remember { mutableStateOf("") }

    //date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    //validation
    var showError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf("") }

    //parse trip dates to milliseconds
    val tripStartMillis = remember(currentTrip) {
        currentTrip?.let { parseDateToMillis(it.startDate) }
    }
    val tripEndMillis = remember(currentTrip) {
        currentTrip?.let { parseDateToMillis(it.endDate) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Itinerary Item") },
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
    ) { paddingValues ->
        if (currentTrip == null) {
            //load trip data
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //trip data range information
                Text(
                    text = "Trip dates: ${currentTrip.startDate} - ${currentTrip.endDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                //item name
                OutlinedTextField(
                    value = itemName,
                    onValueChange = {
                        itemName = it
                        showError = false
                    },
                    label = { Text("Activity Name") },
                    placeholder = { Text("e.g., Wieliczka Salt Mine") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showError && itemName.isBlank()
                )

                //location
                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                        showError = false
                    },
                    label = { Text("Location") },
                    placeholder = { Text("e.g., Krakow Outskirts") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showError && location.isBlank()
                )

                //date field, read only
                OutlinedTextField(
                    value = date,
                    onValueChange = { },
                    label = { Text("Date") },
                    placeholder = { Text("Select date") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = true,
                    isError = showError && date.isBlank(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Select Date"
                            )
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDatePicker = true
                                    }
                                }
                            }
                        }
                )

                //time field, read only
                OutlinedTextField(
                    value = time,
                    onValueChange = { },
                    label = { Text("Time") },
                    placeholder = { Text("Select time") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = true,
                    isError = showError && time.isBlank(),
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Select Time"
                            )
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showTimePicker = true
                                    }
                                }
                            }
                        }
                )

                //optional notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    placeholder = { Text("e.g., Book tickets in advance") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                //error messages
                if (showError) {
                    Text(
                        text = "Please fill in all required fields",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (dateError.isNotEmpty()) {
                    Text(
                        text = dateError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                //save button
                Button(
                    onClick = {
                        if (itemName.isBlank() || location.isBlank() ||
                            date.isBlank() || time.isBlank()) {
                            showError = true
                        } else {
                            val newItem = ItineraryItem(
                                tripId = tripId,
                                name = itemName.trim(),
                                location = location.trim(),
                                date = date.trim(),
                                time = time.trim(),
                                notes = notes.trim()
                            )
                            viewModel.addItineraryItem(newItem)
                            onItemAdded()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Add Item")
                }
            }

            //date picker with restrictions
            if (showDatePicker && tripStartMillis != null && tripEndMillis != null) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = tripStartMillis,
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            // Only allow dates within trip date range
                            return utcTimeMillis >= tripStartMillis && utcTimeMillis <= tripEndMillis
                        }
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                if (millis >= tripStartMillis && millis <= tripEndMillis) {
                                    date = SimpleDateFormat("MMM dd", Locale.getDefault())
                                        .format(Date(millis))
                                    dateError = ""
                                    showDatePicker = false
                                } else {
                                    dateError = "Date must be within trip dates"
                                }
                            }
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            //time picker dialog
            if (showTimePicker) {
                TimePickerDialog(
                    onDismiss = { showTimePicker = false },
                    onConfirm = { hour, minute ->
                        time = String.format("%02d:%02d", hour, minute)
                        showTimePicker = false
                    }
                )
            }
        }
    }
}

//parses date string to milliseconds
private fun parseDateToMillis(dateString: String): Long? {
    return try {
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        format.parse(dateString)?.time
    } catch (e: Exception) {
        try {
            val format = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
            format.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }
}

//custom time picker dialog
//material 3 has a timepicker component but that requires more setup
//this gives us a simpler time input alternative
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(timePickerState.hour, timePickerState.minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
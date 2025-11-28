package com.example.tripplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripplanner.data.Trip
import com.example.tripplanner.ui.viewmodel.TripViewModel

//home screen
//users can view trips, add new trips or delete existing trips

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddTrip: () -> Unit,
    onNavigateToTripDetail: (Int) -> Unit,
    viewModel: TripViewModel = viewModel(factory = TripViewModel.Factory)
){
    //get UI state from viewmodel
    val uiState by viewModel.uiState.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Trips") },
                actions = {
                    //test button to manually trigger worker
                    IconButton(onClick = {
                        //trigger worker
                        val workRequest = androidx.work.OneTimeWorkRequestBuilder<com.example.tripplanner.worker.TripReminderWorker>().build()
                        androidx.work.WorkManager.getInstance(context).enqueue(workRequest)

                        android.widget.Toast.makeText(
                                context, "Background task triggered, check Logcat for 'TripReminderWorker'.", android.widget.Toast.LENGTH_LONG).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Test Background Task"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTrip,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Trip"
                )
            }
        }
    ) { paddingValues ->
        HomeScreenContent(
            trips = uiState.tripList,
            isLoading = uiState.isLoading,
            onTripClick = onNavigateToTripDetail,
            onDeleteTrip = { trip ->
                viewModel.deleteTrip(trip)
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

//stateless content composable for HomeScreen
//separated for easier preview and testing

@Composable
private fun HomeScreenContent(
    trips: List<Trip>,
    isLoading: Boolean,
    onTripClick: (Int) -> Unit,
    onDeleteTrip: (Trip) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            trips.isEmpty() -> {
                EmptyState()
            }
            else -> {
                TripList(
                    trips = trips,
                    onTripClick = onTripClick,
                    onDeleteTrip = onDeleteTrip
                )
            }
        }
    }
}

//what displays when there are no trips
@Composable
private fun EmptyState(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ){
        Text(
            text = "No trips yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to add your first trip",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

//scrollable list of trip cards
@Composable
private fun TripList(
    trips: List<Trip>,
    onTripClick: (Int) -> Unit,
    onDeleteTrip: (Trip) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(trips, key = {trip -> trip.id}){ trip ->
            TripCard(
                trip = trip,
                onClick = {onTripClick(trip.id)},
                onDelete = {onDeleteTrip(trip)}
            )
        }
    }
}

//individual trip card component
//displays trip info and a delete button
@Composable
private fun TripCard(
    trip: Trip,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            //trip info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trip.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${trip.startDate} - ${trip.endDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            //delete button
            IconButton(onClick = onDelete){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Trip",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

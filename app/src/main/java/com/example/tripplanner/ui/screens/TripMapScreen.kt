package com.example.tripplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripplanner.data.ItineraryItem
import com.example.tripplanner.ui.viewmodel.TripViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

//screen that displays itinerary items on Google Maps
//shows markers for each location in the itinerary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripMapScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TripViewModel = viewModel(factory = TripViewModel.Factory)
) {
    //load itinerary items
    LaunchedEffect(tripId){
        viewModel.loadItineraryItems(tripId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val currentTrip = uiState.currentTrip
    val itineraryItems = uiState.itineraryItems

    //find the trip if not already selected
    LaunchedEffect(tripId, uiState.tripList){
        if(currentTrip == null || currentTrip.id != tripId) {
            val trip = uiState.tripList.find {it.id == tripId}
            trip?.let {viewModel.selectTrip(it)}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTrip?.name ?: "Map View") },
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
    ) {
        paddingValues ->
        if (itineraryItems.isEmpty()) {

            //empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No locations to display",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Add itinerary items to see them on the map",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {

            //show map with markers
            TripMap(
                itineraryItems = itineraryItems,
                tripLocation = currentTrip?.location ?: "Unknown",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

//google maps component with markers
@Composable
private fun TripMap(
    itineraryItems: List<ItineraryItem>,
    tripLocation: String,
    modifier: Modifier = Modifier
) {
    //default location, would get replaced with proper geocoding for a production app
    //this is just to show the api integration
    val defaultLocation = getDefaultLocation(tripLocation)

    //camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = false
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false
        )
    )
    {
        //add markers for each itinerary item
        itineraryItems.forEachIndexed {
            index, item ->
            val position = getLocationCoordinates(item.location, tripLocation, index)

            Marker(
                state = MarkerState(position = position),
                title = item.name,
                snippet = "${item.date} at ${item.time}"
            )
        }
    }
}

//gets default location for cities
//wouldnt be used in production app
private fun getDefaultLocation(location: String): LatLng {
    return when {
        //some hardcoded locations
        location.contains("Krakow", ignoreCase = true) || location.contains("Cracow", ignoreCase = true) -> LatLng(50.0647, 19.9450)

        location.contains("Dublin", ignoreCase = true) -> LatLng(53.3498, -6.2603)

        location.contains("Paris", ignoreCase = true) -> LatLng(48.8566, 2.3522)

        location.contains("London", ignoreCase = true) -> LatLng(51.5074, -0.1278)

        location.contains("New York", ignoreCase = true) -> LatLng(40.7128, -74.0060)

        location.contains("Tokyo", ignoreCase = true) -> LatLng(35.6762, 139.6503)

        location.contains("Barcelona", ignoreCase = true) -> LatLng(41.3851, 2.1734)

        location.contains("Rome", ignoreCase = true) -> LatLng(41.9028, 12.4964)

        location.contains("Berlin", ignoreCase = true) -> LatLng(52.5200, 13.4050)

        location.contains("Amsterdam", ignoreCase = true) -> LatLng(52.3676, 4.9041)

        else -> LatLng(0.0, 0.0) //default fallback
    }
}

//gets approximate coordinates for itinerary item locations
private fun getLocationCoordinates(
    itemLocation: String,
    tripLocation: String,
    index: Int
): LatLng {
    val baseLocation = getDefaultLocation(tripLocation)

    //spread markers in a circle around base location
    //just used to approximate location
    val radius = 0.05
    val angle = (index * 60.0) * Math.PI / 180.0 //spread of markers

    val lat = baseLocation.latitude + (radius * Math.cos(angle))
    val lng = baseLocation.longitude + (radius * Math.sin(angle))

    return LatLng(lat, lng)
}
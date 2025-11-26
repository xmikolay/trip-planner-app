package com.example.tripplanner.ui.viewmodel

import android.os.Message
import com.example.tripplanner.data.Trip
import com.example.tripplanner.data.ItineraryItem

/**
 * UI state for the app
 * represents all data needed by the ui at any moment
 */

data class TripUiState( //immutable data class
    val tripList: List<Trip> = emptyList(),
    val currentTrip: Trip? = null,
    val itineraryItems: List<ItineraryItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
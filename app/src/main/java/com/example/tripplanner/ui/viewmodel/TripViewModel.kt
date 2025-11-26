package com.example.tripplanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.tripplanner.TripPlannerApplication
import com.example.tripplanner.data.ItineraryItem
import com.example.tripplanner.data.Trip
import com.example.tripplanner.data.TripsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * viewmodel for managing trip and itinerary items
 * handles logic and maintains UI state
 */

class TripViewModel(private val repository: TripsRepository) : ViewModel() {
    //private mutable state flow
    private val _uiState = MutableStateFlow(TripUiState())

    //public immutable state flow
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow() //read-only

    //all of that stuff above is part of oop, encapsulation
    //and it means that all state changes go only through ViewModel functions
    //very cool

    init{
        loadTrips() //load trips when viewmodel is created
    }

    //loads all trips from repo, observes flow and updates the UI when data changes
    private fun loadTrips(){
        viewModelScope.launch{
            _uiState.update {it.copy(isLoading=true)}

            repository.getAllTrips().collect { trips ->
                _uiState.update {
                    it.copy( //it means current state, copy creates new state with changes that we provide.
                        tripList = trips,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    //load items for specific trip
    fun loadItineraryItems(tripId: Int) {
        viewModelScope.launch {
            repository.getItineraryItemsForTrip(tripId).collect { items ->
                _uiState.update {it.copy(itineraryItems = items)}
            }
        }
    }

    //set currently selected trip
    fun selectTrip(trip: Trip) {
        _uiState.update { it.copy(currentTrip = trip)}
        loadItineraryItems(trip.id)
    }

    //add a new trip to database
    fun addTrip(trip: Trip){
        viewModelScope.launch{
            try{
                repository.insertTrip(trip)
            } catch (e: Exception){
                _uiState.update {
                    it.copy(errorMessage = "Failed to add trip: ${e.message}")
                }
            }
        }
    }

    //update an existing trip
    fun updateTrip(trip: Trip){
        viewModelScope.launch {
            try{
                repository.updateTrip(trip)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to update trip: ${e.message}")
                }
            }
        }
    }

    //delete an existing trip
    fun deleteTrip(trip: Trip){
        viewModelScope.launch {
            try{
                repository.deleteTrip(trip)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete trip: ${e.message}")
                }
            }
        }
    }

    //add itinerary item
    fun addItineraryItem(item: ItineraryItem){
        viewModelScope.launch{
            try{
                repository.insertItineraryItem(item)
            }catch (e: Exception){
                _uiState.update {
                    it.copy(errorMessage = "Failed to add itinerary item: ${e.message}")
                }
            }
        }
    }

    //update itinerary item
    fun updateItineraryItem(item: ItineraryItem){
        viewModelScope.launch {
            try{
                repository.updateItineraryItem(item)
            } catch (e: Exception){
                _uiState.update {
                    it.copy(errorMessage = "Failed to update itinerary item: ${e.message}")
                }
            }
        }
    }

    //delete itinerary item
    fun deleteItineraryItem(item: ItineraryItem){
        viewModelScope.launch {
            try{
                repository.deleteItineraryItem(item)
            }catch (e: Exception){
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete itinerary item: ${e.message}")
                }
            }
        }
    }

    //clear any error messages
    fun clearError(){
        _uiState.update{
            it.copy(errorMessage = null)
        }
    }

    //factory for creating tripviewmodel instances with dependencies
    //we need this because the viewmodel needs the repository parameter
    //also because the factory tells android how to create your viewmodel with the repository
    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TripPlannerApplication)
                val repository = application.container.tripsRepository
                TripViewModel(repository = repository)
            }
        }
    }
}
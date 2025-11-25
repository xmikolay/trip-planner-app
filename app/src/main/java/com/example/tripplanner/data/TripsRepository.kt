package com.example.tripplanner.data

import kotlinx.coroutines.flow.Flow

/**
 * repo for managing trip and itinerary data.
 * provides a clean api for data operation and abstracts the data source
 * acts as a single source of truth for trip-related data
 */

class TripsRepository (private val tripDao: TripDao, private val itineraryDao: ItineraryDao) {

    //trip operations

    //get all trips from db
    fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()

    //get a specific trip by id
    fun getTripById(tripId: Int): Flow<Trip?> = tripDao.getTripById(tripId)

    //insert a new trip into the db
    //suspends the coroutine until the operation completes
    suspend fun insertTrip(trip: Trip){
        tripDao.insertTrip(trip)
    }

    //update an existing trip in the database
    suspend fun updateTrip(trip: Trip){
        tripDao.updateTrip(trip)
    }

    //delete a trip from the db
    //all associated itinerary items will be deleted too
    suspend fun deleteTrip(trip: Trip){
        tripDao.deleteTrip(trip)
    }

    //itinerary operations

    //get all itinerary items for a specific trip, items ordered by date and time
    fun getItineraryItemsForTrip(tripId: Int): Flow<List<ItineraryItem>>{
        return itineraryDao.getItineraryItemsForTrip(tripId)
    }

    //get a specific itinerary item by id
    fun getItineraryItemById(itemId: Int): Flow<ItineraryItem?>{
        return itineraryDao.getItineraryItemById(itemId)
    }

    //insert a new itinerary item into the db
    suspend fun insertItineraryItem(item: ItineraryItem) {
        itineraryDao.insertItineraryItem(item)
    }

    //update existing item
    suspend fun updateItineraryItem(item: ItineraryItem){
        itineraryDao.updateItineraryItem(item)
    }

    //delete item
    suspend fun deleteItineraryItem(item: ItineraryItem){
        itineraryDao.deleteItineraryItem(item)
    }

    //delete all items for specific trip
    suspend fun deleteAllItemsForTrip(tripId: Int){
        itineraryDao.deleteAllItemsForTrip(tripId)
    }
}
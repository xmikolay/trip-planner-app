package com.example.tripplanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object: defines database operations for trips.
 */

@Dao
interface TripDao {
    //get all trips from database
    //returns a flow that emits updated list whenever data changes
    @Query("SELECT * FROM trips ORDER BY startDate ASC") //using SQL queries
    fun getAllTrips(): Flow<List<Trip>> //flow is like an IObservable<> in C#

    //get a single trip by Id
    //returns null if trip doesnt exist
    @Query("SELECT * FROM trips WHERE id = :tripId")
    fun getTripById(tripId: Int): Flow<Trip?> //? means nullable

    //insert a new trip into the db
    //if trip already exists, has same id, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip) //suspend is like kotlins async/await

    //update an existing trip in the db
    @Update
    suspend fun updateTrip(trip: Trip)

    //delete a trip from the db
    //cascade delete will remove associated itinerary items by itself
    @Delete
    suspend fun deleteTrip(trip: Trip)
}
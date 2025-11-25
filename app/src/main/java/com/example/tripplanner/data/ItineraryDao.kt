package com.example.tripplanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO for itinerary items
 */

@Dao
interface ItineraryDao {

    //get all itinerary items for a trip
    //returns a flow that updates when data changes
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId ORDER BY date ASC, time ASC")
    fun getItineraryItemsForTrip(tripId: Int): Flow<List<ItineraryItem>>

    //get a single item by id
    @Query("SELECT * FROM itinerary_items WHERE id = :itemId")
    fun getItineraryItemById(itemId: Int): Flow<ItineraryItem?>

    //insert a new itinerary item into db
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItem(item: ItineraryItem)

    //update an existing itinerary item
    @Update
    suspend fun updateItineraryItem(item: ItineraryItem)

    //delete item
    @Delete
    suspend fun deleteItineraryItem(item: ItineraryItem)

    //Delete all itinerary items for a specific trip
    @Query("DELETE FROM itinerary_items WHERE tripId = :tripId")
    suspend fun deleteAllItemsForTrip(tripId: Int)
}
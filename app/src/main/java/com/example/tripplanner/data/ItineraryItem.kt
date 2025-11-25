package com.example.tripplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

/**
 * Entity representing an itinerary item in the database.
 * Each item belongs to a specific trip and includes location, date, time and notes
 */

@Entity(
    tableName = "itinerary_items",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE //delete items when trip is deleted
        )
    ]
)

data class ItineraryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int, //foreign key to trip
    val name: String,
    val location: String,
    val date: String,
    val time: String,
    val notes: String = ""
)
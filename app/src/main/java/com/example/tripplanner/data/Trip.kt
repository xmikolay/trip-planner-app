package com.example.tripplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a trip in the database, each trip has a name,
 * location, start date and end date.
 */

@Entity(tableName = "trips") //entity marks this as a database table
data class Trip(
    @PrimaryKey(autoGenerate = true) //unique identifier for each row, and then room auto generates IDs
    val id: Int = 0,
    val name: String,
    val location: String,
    val startDate: String,
    val endDate: String
)
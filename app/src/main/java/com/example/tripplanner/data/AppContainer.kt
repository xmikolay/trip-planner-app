package com.example.tripplanner.data

import android.content.Context

/**
 * Application level dependency container
 */

interface AppContainer {
    val tripsRepository: TripsRepository
}

//implementation of AppContainer that provides actual instances, uses lazy initialization
//to create dependencys only when needed

class AppDataContainer(private val context: Context) : AppContainer{
    override val tripsRepository: TripsRepository by lazy{
        TripsRepository(
            tripDao = TripDatabase.getDatabase(context).tripDao(),
            itineraryDao = TripDatabase.getDatabase(context).itineraryDao()
        )
    }
}
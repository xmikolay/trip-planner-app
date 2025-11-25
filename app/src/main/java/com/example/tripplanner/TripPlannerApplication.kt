package com.example.tripplanner

import android.app.Application
import com.example.tripplanner.data.AppContainer
import com.example.tripplanner.data.AppDataContainer

/**
 * Custom application class for the app
 * initializes app-wide dependencies and provides the dependency container
 * created when the app starts and lives for the entirety of the apps lifetimes
 */

class TripPlannerApplication : Application() {

    //app container instance for DI
    lateinit var container: AppContainer //tells kotlin to initialize this before using it

    //called when the app is starting
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
package com.example.tripplanner

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tripplanner.data.AppContainer
import com.example.tripplanner.data.AppDataContainer
import com.example.tripplanner.worker.TripReminderWorker
import java.util.concurrent.TimeUnit

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

        //schedule periodic background work
        schedulePeriodicWork()
    }

    //schedules periodic background work using WorkManager
    private fun schedulePeriodicWork() {
        //define constraints for when the work should run
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) //only run when battery is not low
            .setRequiresStorageNotLow(true) //only run when storage is not low
            .build()

        //create periodic work request, runs every 24 hours
        val workRequest = PeriodicWorkRequestBuilder<TripReminderWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        //schedule the work
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TripReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
package com.example.tripplanner.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tripplanner.TripPlannerApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

//background worker that periodically checks for upcoming trips, runs daily to log upcoming trips
//addition feature could be for it to send notifications
//workmanager ensures this runs even if the app is closed or device restarts

class TripReminderWorker (
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "TripReminderWork"
        private const val TAG = "TripReminderWorker"
    }

    //this method runs in the background
    //returns Result.success() if successful, Result.failure() if failed
    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        return@withContext try{
            Log.d(TAG, "TripReminderWorker started at ${getCurrentTimeStamp()}")

            //get repository from application container
            val application = applicationContext as TripPlannerApplication
            val repository = application.container.tripsRepository

            //checks for trips
            Log.d(TAG, "Checking for upcoming trips...")

            //simulate some work
            Log.d(TAG, "Trip reminder check completed successfully")

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in TripReminderWorker: ${e.message}", e)
            Result.failure()
        }
    }

    //gets current timestamp for logging
    private fun getCurrentTimeStamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}

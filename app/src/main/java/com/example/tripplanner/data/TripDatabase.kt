package com.example.tripplanner.data

import androidx.room.Database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room db for the app
 * defines the db config and provides DAO instances
 */

@Database(
    entities = [Trip::class, ItineraryItem::class],
    version = 1,
    exportSchema = false
)

abstract class TripDatabase : RoomDatabase() { //like a dbcontext

    //provides access to trip data operations
    abstract fun tripDao(): TripDao

    //provides access to itinerary item data operations
    abstract fun itineraryDao(): ItineraryDao

    companion object { //like a static member
        //singleton instances of the db
        //volatile ensures changes are immediately visible to other threads

        @Volatile
        private var INSTANCE: TripDatabase? = null

        //gets the singleton db instance, creates it if it doesnt exist yet.
        //thread safe using synchronized block.

        fun getDatabase(context: Context): TripDatabase{
            //if it already exists just return it
            return INSTANCE ?: synchronized(this){ //If INSTANCE exists, return it. Otherwise, run this synchronized block.
                //double check
                val instance = Room.databaseBuilder(
                    context.applicationContext, //avoid memory leaks
                    TripDatabase::class.java, //db class type, need java for room
                    "trip_database" //db file name
                )
                    .fallbackToDestructiveMigration() //If you don't have migration logic, just delete the old database and create a new one, so will get changed for final product
                    .build() //creates db instance

                INSTANCE = instance
                instance
            }
        }
    }
}
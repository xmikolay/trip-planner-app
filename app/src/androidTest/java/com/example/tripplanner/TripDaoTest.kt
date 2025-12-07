package com.example.tripplanner

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tripplanner.data.Trip
import com.example.tripplanner.data.TripDao
import com.example.tripplanner.data.TripDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

//instrumentation tests for TripDao
//tests with database operations on an actual emulator/device

@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var database: TripDatabase
    private lateinit var tripDao: TripDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            TripDatabase::class.java
        ).build()

        tripDao = database.tripDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertTrip_andRetrieveById() = runBlocking {
        val trip = Trip(
            id = 1,
            name = "Krakow Trip",
            location = "Krakow, Poland",
            startDate = "Nov 26, 2025",
            endDate = "Nov 30, 2025"
        )

        tripDao.insertTrip(trip)

        val retrieved = tripDao.getTripById(1).first()
        assertNotNull("Trip should be retrieved", retrieved)
        assertEquals("Trip name should match", "Krakow Trip", retrieved?.name)
    }

    @Test
    fun insertMultipleTrips_andGetAll() = runBlocking {
        val trip1 = Trip(
            id = 1,
            name = "Dublin Trip",
            location = "Dublin, Ireland",
            startDate = "Dec 01, 2025",
            endDate = "Dec 05, 2025"
        )
        val trip2 = Trip(
            id = 2,
            name = "Paris Trip",
            location = "Paris, France",
            startDate = "Dec 10, 2025",
            endDate = "Dec 15, 2025"
        )

        tripDao.insertTrip(trip1)
        tripDao.insertTrip(trip2)

        val allTrips = tripDao.getAllTrips().first()
        assertEquals("Should have 2 trips", 2, allTrips.size)
    }

    @Test
    fun deleteTrip_removesFromDatabase() = runBlocking {
        val trip = Trip(
            id = 1,
            name = "Test Trip",
            location = "Test Location",
            startDate = "Jan 01, 2025",
            endDate = "Jan 05, 2025"
        )
        tripDao.insertTrip(trip)

        tripDao.deleteTrip(trip)

        val allTrips = tripDao.getAllTrips().first()
        assertTrue("Trip list should be empty", allTrips.isEmpty())
    }
}
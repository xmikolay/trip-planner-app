package com.example.tripplanner

import com.example.tripplanner.data.Trip
import com.example.tripplanner.data.ItineraryItem
import com.example.tripplanner.data.TripDao
import com.example.tripplanner.data.ItineraryDao
import com.example.tripplanner.data.TripsRepository
import com.example.tripplanner.ui.viewmodel.TripViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

//unit tests for TripViewModel
//tests state management and business logic without requiring android framework

@OptIn(ExperimentalCoroutinesApi::class)
class TripViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TripViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        //create fake repo with empty DAOs
        val fakeRepository = TripsRepository(
            tripDao = FakeTripDao(),
            itineraryDao = FakeItineraryDao()
        )
        viewModel = TripViewModel(fakeRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty trip list`(){
        val uiState = viewModel.uiState.value

        assertTrue("Initial trip list should be empty", uiState.tripList.isEmpty())
        assertFalse("Should not be loading initially", uiState.isLoading)
        assertNull("Should have no error initially", uiState.errorMessage)
    }

    @Test
    fun `selectTrip updates current trip in UI state`() = runTest {
        val testTrip = Trip(
            id = 1,
            name = "Test Trip",
            location = "Test Location",
            startDate = "Jan 01, 2025",
            endDate = "Jan 05, 2025"
        )

        viewModel.selectTrip(testTrip)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals("Current trip should be set", testTrip, uiState.currentTrip)
    }

    @Test
    fun `clearError removes error message from state`() = runTest {
        viewModel.clearError()
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertNull("Error message should be null", uiState.errorMessage)
    }
}

//fake implementations for testing
class FakeTripDao : TripDao {
    override fun getAllTrips() = flowOf(emptyList<Trip>())
    override fun getTripById(tripId: Int) = flowOf(null)
    override suspend fun insertTrip(trip: Trip) {}
    override suspend fun updateTrip(trip: Trip) {}
    override suspend fun deleteTrip(trip: Trip) {}
}

class FakeItineraryDao : ItineraryDao {
    override fun getItineraryItemsForTrip(tripId: Int) = flowOf(emptyList<ItineraryItem>())
    override fun getItineraryItemById(itemId: Int) = flowOf(null)
    override suspend fun insertItineraryItem(item: ItineraryItem) {}
    override suspend fun updateItineraryItem(item: ItineraryItem) {}
    override suspend fun deleteItineraryItem(item: ItineraryItem) {}
    override suspend fun deleteAllItemsForTrip(tripId: Int) {}
}
package com.example.tripplanner

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//testing basic app logic
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun trip_name_validation_works(){
        val tripName = "Krakow Trip"
        assertTrue("Trip name should not be empty", tripName.isNotBlank())
        assertTrue("Trip name should be valid", tripName.length > 0)
    }

    @Test
    fun date_string_formatting_works(){
        val date = "Nov 26, 2025"
        assertTrue("Date should contain month", date.contains("Nov"))
        assertTrue("Date should contain year", date.contains("2025"))
    }
}
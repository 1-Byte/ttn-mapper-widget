package net.gmx.onebyte.ttnmapper

import org.junit.Assert.*
import org.junit.Test

class LocationTest {
    @Test
    fun testDistanceTo() {
        val distance = Location(34.0, 5.0, 1000.0)
                .distanceTo(Location(34.0, 6.0, 5000.0))
        assertEquals(92513.5, distance, 1.0)
    }

    @Test
    fun testDistanceToAltitude() {
        val distance = Location(34.0, 5.0, 1000.0)
                .distanceTo(Location(34.0, 5.0, 5000.0))
        assertEquals(4000.0, distance, 1.0)
    }

    @Test
    fun testToEcef() {
        val point = Location(34.0, 15.0, 2000.0).toEcef()
        assertEquals(5114497.0, point.x, 1.0)
        assertEquals(1370425.0, point.y, 1.0)
        assertEquals(3547565.0, point.z, 1.0)
    }
}
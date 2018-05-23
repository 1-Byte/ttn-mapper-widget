package net.gmx.onebyte.ttnmapper

private const val WGS84_A = 6378137.0
private const val WGS84_E2 = 6.6943799901377997e-3;

data class Point(val x: Double, val y: Double, val z: Double) {
    fun distanceTo(other: Point): Double {
        return Math.sqrt(
                (x - other.x) * (x - other.x) +
                        (y - other.y) * (y - other.y) +
                        (z - other.z) * (z - other.z)
        )
    }
}

data class Location(val latitude: Double, val longitude: Double, val altitude: Double) {
    fun distanceTo(other: Location): Double {
        return toEcef().distanceTo(other.toEcef())
    }

    fun toEcef(): Point {
        val latitudeSin = Math.sin(Math.toRadians(latitude))
        val latitudeCos = Math.cos(Math.toRadians(latitude))
        val n = WGS84_A / Math.sqrt(1 - WGS84_E2 * latitudeSin * latitudeSin)
        return Point(
                (n + altitude) * latitudeCos * Math.cos(Math.toRadians(longitude)),
                (n + altitude) * latitudeCos * Math.sin(Math.toRadians(longitude)),
                (n * (1 - WGS84_E2) + altitude) * latitudeSin
        )
    }
}
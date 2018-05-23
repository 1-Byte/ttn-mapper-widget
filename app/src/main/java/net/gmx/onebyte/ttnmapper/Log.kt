package net.gmx.onebyte.ttnmapper

data class Log(
        val id: Int,
        val time: String,
        val gateway: String,
        val location: Location,
        val snr: Double,
        val rssi: Double,
        val hdop: Double,
        val satellites: Int)
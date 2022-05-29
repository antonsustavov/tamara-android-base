package com.tamara.care.watch.data.entity


data class ProximityItemEntity(
    val _id: String?,
    val transmitter: List<TransmitterEntity>,
    val closestRoom: String?,
    val closestRoomSignal: Int?,
    val veryClose: String?,
    val veryCloseSignal: Int?,
    val dateTime: String?,
)



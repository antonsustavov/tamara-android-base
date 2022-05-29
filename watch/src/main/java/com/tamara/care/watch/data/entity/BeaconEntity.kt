package com.tamara.care.watch.data.entity

data class BeaconEntity(
    val transmitter: String? = null,
    val closestRoom: String? = null,
    val closestRoomSignal: Int? = null,
    val veryClose: String? = null,
    val veryCloseSignal: Int? = null,
    val dateTime: String? = null,
    //val signals: List<BeaconInfoEntity>
)


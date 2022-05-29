package com.tamara.care.watch.data.entity

data class BeaconEntity(
    val transmitter: String,
    val signals: List<BeaconInfoEntity> = emptyList()
)



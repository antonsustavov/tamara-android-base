package com.tamara.care.watch.data.entity

data class ParametersEntity(
    val heartRate: String,
    val accelerometer: String?,
    val temperature: String?,
    val gyroscope: String?,
    val transmitter: List<TransmitterEntity>?,
    val dateTime: String?,
    val gyroChanges: String?,
)



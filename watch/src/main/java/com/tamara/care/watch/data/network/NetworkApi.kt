package com.tamara.care.watch.data.network

import com.tamara.care.watch.data.entity.BeaconEntity
import com.tamara.care.watch.data.entity.BeaconsListEntity
import com.tamara.care.watch.data.entity.TransmitterEntity
import com.tamara.care.watch.data.entity.WatchInfoRequestEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkApi {

    @POST("proximity")
    suspend fun sendBeaconInfo(
        @Body beaconEntity: BeaconEntity
    ): Response<BeaconEntity>

    @POST("transmitters")
    suspend fun sendTransmitterInfo(
        @Body transmitterEntity: TransmitterEntity
    ): Response<TransmitterEntity>

    @POST("physical-parameters")
    suspend fun sendParametersInfo(
        @Body parametersEntity: List<WatchInfoRequestEntity>
    ): Response<WatchInfoRequestEntity>

    @GET("transmitters/{id}/beacons")
    suspend fun getBeaconInfo(
        @Path("id") transmitterId: String?
    ): Response<BeaconsListEntity>
}
package com.tamara.care.watch.data.network

import com.tamara.care.watch.data.entity.*
import retrofit2.Response
import retrofit2.http.*

interface NetworkApi {

    @GET("transmitters/{id}/beacons")
    suspend fun getBeaconInfo(
        @Path("id") transmitterId: String?
    ): Response<BeaconsListEntity>

    @GET("transmitters/{id}")
    suspend fun getTransmitterInfo(
        @Path("id") id: String,
    ): Response<TransmitterListEntity>

    @POST("beacons")
    suspend fun saveBeaconInfo(
        @Body beaconRegisterEntity: BeaconRegisterEntity
    ): Response<BeaconRegisterEntity>

    @PUT("beacons/{id}")
    suspend fun updateBeaconInfo(
        @Path("id") id: String,
        @Body beaconRegisterEntity: BeaconRegisterEntity
    ): Response<BeaconRegisterEntity>

    @DELETE("beacons/{id}")
    suspend fun deleteBeaconInfo(
        @Path("id") id: String,
    ): Response<BeaconRegisterEntity>

    @POST("beacons")
    suspend fun registerBeacon(
        @Body beaconRegisterEntity: BeaconRegisterEntity
    ): Response<BeaconEntity>

    @GET("proximity")
    suspend fun getProximityInfo(
        @Query("transmitterId") transmitterId: String? = null,
        @Query("limit") limit: Int? = 20,
        @Query("offset") offset: Int,
    ): Response<ProximityEntity>

    @GET("proximity/history")
    suspend fun getRoomInfo(
        @Query("transmitterId") transmitterId: String? = null,
        @Query("period") period: String? = null,
        @Query("dateFrom") dateFrom: String? = null,
        @Query("dateTo") dateTo: String? = null,
        @Query("room") room: String? = null,
        @Query("sortBy") sortBy: String = "dateTime",
        @Query("orderBy") orderBy: String = "desc",
    ): Response<List<RoomEntity>>
//    @GET("therapist/{id}")
//    suspend fun getTherapistInfo(
//        @Path("id") id: String? = null
//    ): Response<TherapistEntity>

    @GET("physical-parameters")
    suspend fun getParametersInfo(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int = 20,
        @Query("transmitterId") transmitterId: String?,
    ): Response<ParametersListEntity>
}
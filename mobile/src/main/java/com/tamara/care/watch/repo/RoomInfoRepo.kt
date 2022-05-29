package com.tamara.care.watch.repo

import com.tamara.care.watch.data.entity.RoomEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomInfoRepo @Inject constructor(
    private var networkApi: NetworkApi,
) : BaseRepo() {

    suspend fun getRoomInfo(
        transmitterId: String? = null,
        roomName: String,
        period: String? = null,
        dateFrom: String? = null,
        dateTo: String? = null,
    ): ModelState<List<RoomEntity>> {
        return try {
            val response = networkApi.getRoomInfo(
                transmitterId = transmitterId,
                room = roomName,
                period = period,
                dateFrom = dateFrom,
                dateTo = dateTo
            )
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!
                )

            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            handleError()
        }
    }
}
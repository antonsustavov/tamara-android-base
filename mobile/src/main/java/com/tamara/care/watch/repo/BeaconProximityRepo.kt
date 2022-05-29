package com.tamara.care.watch.repo

import com.tamara.care.watch.data.entity.ProximityEntity
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeaconProximityRepo @Inject constructor(
    private var networkApi: NetworkApi,
) : BaseRepo() {


    suspend fun getProximityInfo(transmitterId: String? = null, offset: Int): ModelState<ProximityEntity> {
        return try {
            val response = networkApi.getProximityInfo(
                transmitterId = transmitterId, offset = offset
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
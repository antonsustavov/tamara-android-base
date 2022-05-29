package com.tamara.care.watch.repo

import com.tamara.care.watch.data.entity.ParametersListEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import com.tamara.care.watch.data.entity.TransmitterListEntity

@Singleton
class ParametersRepo @Inject constructor(
    private var networkApi: NetworkApi,
) : BaseRepo() {

    suspend fun getParametersInfo(offset: Int? = null, transmitterId: String? = null): ModelState<ParametersListEntity> {
        return try {
            val response = networkApi.getParametersInfo(
                offset = offset,
                transmitterId = transmitterId
            )
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!
                )
            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            Log.e("TAG", e.message.toString())
            handleError()
        }
    }

    suspend fun checkIfTransmitterIsOnHand(transmitterId: String?): ModelState<TransmitterListEntity> {
        return try {
            val response = networkApi.getTransmitterInfo(transmitterId ?: "")
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!
                )
            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            Log.e("TAG", e.message.toString())
            handleError()
        }
    }
}
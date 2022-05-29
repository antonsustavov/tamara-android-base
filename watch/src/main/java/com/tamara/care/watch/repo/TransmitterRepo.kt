package com.tamara.care.watch.repo


import com.tamara.care.watch.data.entity.TransmitterEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransmitterRepo @Inject constructor(
    private var networkApi: NetworkApi,
) : BaseRepo() {


    suspend fun sendTransmitterInfo(transmitterModel: TransmitterEntity): ModelState<TransmitterEntity> {
        return try {
            val response = networkApi.sendTransmitterInfo(transmitterModel)
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
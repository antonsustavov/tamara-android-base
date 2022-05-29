package com.tamara.care.watch.repo

import com.tamara.care.watch.data.entity.WatchInfoRequestEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.data.room.WatchInfoEntity
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParametersRepo @Inject constructor(
    private var networkApi: NetworkApi,
) : BaseRepo() {

    suspend fun sendParametersInfo(parametersList: List<WatchInfoRequestEntity>): ModelState<WatchInfoRequestEntity> {
        return try {
            val response = networkApi.sendParametersInfo(parametersList)
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
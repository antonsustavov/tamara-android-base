package com.tamara.care.watch.repo

import com.tamara.care.watch.data.entity.BeaconRegisterEntity
import com.tamara.care.watch.data.entity.BeaconsListEntity
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class BeaconInfoRepo @Inject constructor(
    private var networkApi: NetworkApi,
) : BaseRepo() {


    suspend fun getBeaconInfo(transmitterId:String?): ModelState<BeaconsListEntity> {
        return try {
            val response = networkApi.getBeaconInfo(transmitterId)
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!)
            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            Log.e("ErrorTAG", e.message.toString())
            handleError()
        }
    }

    suspend fun saveBeaconInfo( beaconRegisterEntity: BeaconRegisterEntity): ModelState<BeaconRegisterEntity> {
        return try {
            val response = networkApi.saveBeaconInfo(beaconRegisterEntity)
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!)
            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            handleError()
        }
    }

    suspend fun updateBeaconInfo(id:String, beaconRegisterEntity: BeaconRegisterEntity): ModelState<BeaconRegisterEntity> {
        return try {
            val response = networkApi.updateBeaconInfo(id, beaconRegisterEntity)
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!)
            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            handleError()
        }
    }

    suspend fun deleteBeaconInfo(id:String): ModelState<BeaconRegisterEntity> {
        return try {
            val response = networkApi.deleteBeaconInfo(id)
            return if (response.isSuccessful) {
                ModelState.Success(
                    response.body()!!)
            } else {
                handleError(response)
            }
        } catch (e: Exception) {
            handleError()
        }
    }
}
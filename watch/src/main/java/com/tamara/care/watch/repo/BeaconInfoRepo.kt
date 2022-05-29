package com.tamara.care.watch.repo

import android.util.Log
import com.minew.beaconplus.sdk.MTPeripheral
import com.tamara.care.watch.data.entity.BeaconEntity
import com.tamara.care.watch.data.entity.BeaconsListEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.data.network.NetworkApi
import com.tamara.care.watch.data.room.WatchInfoDao
import com.tamara.care.watch.data.room.WatchInfoEntity
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.repo.base.BaseRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeaconInfoRepo @Inject constructor(
    private var networkApi: NetworkApi,
    private var sharedPreferencesManager: SharedPreferencesManager,
    private var watchInfoDao: WatchInfoDao
) : BaseRepo() {
    private suspend fun sendBeaconInfo(beaconEntity: BeaconEntity): ModelState<BeaconEntity> {
        return try {
            val response = networkApi.sendBeaconInfo(beaconEntity)
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

    suspend fun getAllBeacons(): ModelState<BeaconsListEntity> {
        return try {
            val response = networkApi.getBeaconInfo(sharedPreferencesManager.transmitterId)
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

    suspend fun getAllBeaconsAndSendBeaconInfo(
        peripheralsList: List<MTPeripheral>,
        currentDateAndTime: String
    ) {
        try {
            val allBeaconsInfoResponse = networkApi.getBeaconInfo(sharedPreferencesManager.transmitterId)
            if (allBeaconsInfoResponse.isSuccessful) {
                var closestRoomName: String? = null
                var closestRoomSignal: Int? = null
                var veryCloseRoomName: String? = null
                var veryCloseRoomSignal: Int? = null

                peripheralsList.forEach { peripheral ->
                    val beaconEntity = allBeaconsInfoResponse.body()?.items?.find { it.macAddress == peripheral.mMTFrameHandler?.mac }
                    if (beaconEntity != null) {
                        when {
                            closestRoomName.isNullOrEmpty() -> {
                                closestRoomName = beaconEntity.room
                                closestRoomSignal = peripheral.mMTFrameHandler.rssi
                            }
                            veryCloseRoomName.isNullOrEmpty() -> {
                                veryCloseRoomName = beaconEntity.room
                                veryCloseRoomSignal = peripheral.mMTFrameHandler.rssi
                            }
                            else -> {
                                return@forEach
                            }
                        }
                    }
                }

                sendBeaconInfo(
                    BeaconEntity(
                        transmitter = sharedPreferencesManager.transmitterId ?: "-1",
                        closestRoom = closestRoomName,
                        closestRoomSignal = closestRoomSignal,
                        veryClose = veryCloseRoomName,
                        veryCloseSignal = veryCloseRoomSignal,
                        dateTime = currentDateAndTime
                    )
                )
            } else {
                handleError(allBeaconsInfoResponse)
            }
        } catch (e: Exception) {
            handleError()
        }
    }

    suspend fun saveData(watchInfoEntity: WatchInfoEntity) {
        try {
            watchInfoDao.insert(watchInfoEntity)
        } catch (e: Exception) {
            Log.e("SavingDataToDB", e.message.toString())
        }
    }

    suspend fun clearTable() {
        try {
            watchInfoDao.nukeTable()
        } catch (e: Exception) {
            Log.e("NukeDataFromDB", e.message.toString())
        }
    }

    suspend fun getAllInfoFromTable(): List<WatchInfoEntity> {
        return try {
            watchInfoDao.getAllInfo()
        } catch (e: Exception) {
            Log.e("GetDataFromDB", e.message.toString())
            listOf()
        }
    }
}
package com.tamara.care.watch.presentation.room

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.*
import com.tamara.care.watch.presentation.base.BaseViewModel
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.repo.BeaconInfoRepo
import com.tamara.care.watch.repo.BeaconProximityRepo
import com.tamara.care.watch.repo.RoomInfoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomInfoRepo: RoomInfoRepo,
    val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val roomInfoLiveData = MutableLiveData<ModelState<List<RoomEntity>>>()


    fun getRoomInfo(
        roomName: String,
        period: String? = null,
        dateFrom: String? = null,
        dateTo: String? = null,
    ) {
        launchRequest(roomInfoLiveData) {
            roomInfoRepo.getRoomInfo(
                transmitterId = sharedPreferencesManager.transmitterId,
                roomName = roomName,
                period = period,
                dateFrom = dateFrom,
                dateTo = dateTo
            )
        }
    }
}
package com.tamara.care.watch.presentation.setup

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.BeaconRegisterEntity
import com.tamara.care.watch.data.entity.BeaconsListEntity
import com.tamara.care.watch.data.entity.TransmitterListEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.base.BaseViewModel
import com.tamara.care.watch.repo.BeaconInfoRepo
import com.tamara.care.watch.repo.TransmitterRepo
import com.tamara.care.watch.utils.BluetoothState
import com.tamara.care.watch.utils.BluetoothUtils
import com.tamara.care.watch.utils.forceRefresh
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val beaconInfoRepo: BeaconInfoRepo,
    private val transmitterRepo: TransmitterRepo,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val bluetoothUtils: BluetoothUtils
) : BaseViewModel() {

    val beaconsNetworkLiveData = MutableLiveData<ModelState<BeaconsListEntity>>()
    val beaconsInfoLiveData = MutableLiveData<ModelState<BeaconRegisterEntity>>()
    val transmitterInfoLiveData = MutableLiveData<ModelState<TransmitterListEntity>>()

    fun getBeaconInfo() {
        launchRequest(beaconsNetworkLiveData) {
            beaconInfoRepo.getBeaconInfo(sharedPreferencesManager.transmitterId)
        }
    }

    fun saveBeaconInfo(beaconRegisterEntity: BeaconRegisterEntity) {
        launchRequest(beaconsInfoLiveData) {
            beaconInfoRepo.saveBeaconInfo(beaconRegisterEntity)
        }
    }

    fun updateBeaconInfo(id: String, beaconRegisterEntity: BeaconRegisterEntity) {
        launchRequest(beaconsInfoLiveData) {
            beaconInfoRepo.updateBeaconInfo(id, beaconRegisterEntity)
        }
    }

    fun deleteBeaconInfo(id: String) {
        launchRequest {
            beaconInfoRepo.deleteBeaconInfo(id)
        }
    }

    fun getTransmitter(id: String) {
        launchRequest(transmitterInfoLiveData) {
            transmitterRepo.getTransmitterInfo(id)
        }
    }

    fun refreshValue() {
        beaconsNetworkLiveData.forceRefresh()
    }

    fun getBluetoothState(): BluetoothState {
        return bluetoothUtils.bluetoothState()
    }
}
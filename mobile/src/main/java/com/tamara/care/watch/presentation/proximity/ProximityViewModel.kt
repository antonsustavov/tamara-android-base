package com.tamara.care.watch.presentation.proximity

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.ProximityEntity
import com.tamara.care.watch.presentation.base.BaseViewModel
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.repo.BeaconProximityRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProximityViewModel @Inject constructor(
    private val beaconProximityRepo: BeaconProximityRepo,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val proximityInfoLiveData = MutableLiveData<ModelState<ProximityEntity>>()


    fun getProximityInfo(offset: Int) {
        launchRequest(proximityInfoLiveData) {
            beaconProximityRepo.getProximityInfo(
                transmitterId = sharedPreferencesManager.transmitterId,
                offset = offset
            )
        }
    }
}
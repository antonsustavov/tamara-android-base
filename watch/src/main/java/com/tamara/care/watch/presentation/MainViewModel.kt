package com.tamara.care.watch.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.BeaconsListEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.repo.BeaconInfoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by ArtemLampa on 06.10.2021.
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val beaconInfoRepo: BeaconInfoRepo
) : BaseViewModel() {

    private val _allBeaconsLiveData = MutableLiveData<ModelState<BeaconsListEntity>>()
    val allBeaconsLiveData: LiveData<ModelState<BeaconsListEntity>> get() = _allBeaconsLiveData

    fun getAllBeacons() {
        launchRequest(_allBeaconsLiveData) {
            beaconInfoRepo.getAllBeacons()
        }
    }
}
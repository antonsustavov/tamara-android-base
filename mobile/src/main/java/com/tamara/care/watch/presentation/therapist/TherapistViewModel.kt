package com.tamara.care.watch.presentation.therapist

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.*
import com.tamara.care.watch.presentation.base.BaseViewModel
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.repo.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TherapistViewModel @Inject constructor(
    private val therapistRepo: TherapistRepo,
    private val transmitterRepo: TransmitterRepo,
    val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val therapistInfoLiveData = MutableLiveData<ModelState<TransmitterListEntity>>()


    fun getTherapistInfo() {
        launchRequest(therapistInfoLiveData) {
            transmitterRepo.getTransmitterInfo(sharedPreferencesManager.transmitterId ?: "")
        }
    }
}
package com.tamara.care.watch.presentation.patient

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.ParametersListEntity
import com.tamara.care.watch.data.entity.TransmitterListEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.base.BaseViewModel
import com.tamara.care.watch.repo.ParametersRepo
import com.tamara.care.watch.repo.TransmitterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by ArtemLampa on 12.10.2021.
 */

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val parametersRepo: ParametersRepo,
    private val transmitterRepo: TransmitterRepo,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {
    val physicalParameterLiveData = MutableLiveData<ModelState<ParametersListEntity>>()
    val therapistInfoLiveData = MutableLiveData<ModelState<TransmitterListEntity>>()

    fun getPhysicalParameters(offset: Int, transmitterId: String?) {
        launchRequest(physicalParameterLiveData) {
            parametersRepo.getParametersInfo(
                offset = offset,
                transmitterId = transmitterId
            )
        }
    }

    fun getPatientInfo() {
        launchRequest(therapistInfoLiveData) {
            transmitterRepo.getTransmitterInfo(sharedPreferencesManager.transmitterId ?: "")
        }
    }
}
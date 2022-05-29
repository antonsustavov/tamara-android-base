package com.tamara.care.watch.presentation

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.*
import com.tamara.care.watch.presentation.base.BaseViewModel
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.repo.ParametersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val parametersRepo: ParametersRepo,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val parametersLiveData = MutableLiveData<ModelState<TransmitterListEntity>>()

    fun getParametersInfo() {
        launchRequest(parametersLiveData) {
            parametersRepo.checkIfTransmitterIsOnHand(sharedPreferencesManager.transmitterId)
        }
    }
}
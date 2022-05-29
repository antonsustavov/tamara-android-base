package com.tamara.care.watch.presentation

import androidx.lifecycle.MutableLiveData
import com.tamara.care.watch.data.entity.TransmitterEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.repo.TransmitterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransmitterViewModel @Inject constructor(
    private val transmitterRepo: TransmitterRepo
) : BaseViewModel() {

    val registerTransmitterLiveData = MutableLiveData<ModelState<TransmitterEntity>>()
    val nameLiveData = MutableLiveData("")

    fun sendTransmitterInfo() {

        launchRequest(registerTransmitterLiveData) {
            transmitterRepo.sendTransmitterInfo(
                TransmitterEntity(
                    name = nameLiveData.value,
                    kind = "NURSE",
                    appType = "WATCH"
                )
            )
        }
    }
}
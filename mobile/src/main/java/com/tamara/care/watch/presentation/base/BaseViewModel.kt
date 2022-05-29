package com.tamara.care.watch.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamara.care.watch.data.model.ErrorModel
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.manager.ConnectionManager
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class BaseViewModel() : ViewModel() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    protected fun <T> launchRequest(
        result: MutableLiveData<ModelState<T>>? = null,
        context: CoroutineContext = Dispatchers.IO,
        scope: CoroutineScope = viewModelScope,
        request: suspend CoroutineScope.() -> ModelState<T>,
    ): Job {
        return scope.launch {
            try {
                result?.postValue(ModelState.Loading)
                if (connectionManager.isWiFiConnected.value == false) {
                    result?.postValue(ModelState.Error(ErrorModel(ErrorModel.CONNECTION_ERROR)))
                } else {
                    withContext(context) { request() }.apply {
                        result?.postValue(this)
                    }
                }
            } catch (e: Exception) {
                result?.postValue(ModelState.Error(ErrorModel(ErrorModel.SOMETHING_WENT_WRONG)))
            }
        }
    }
}
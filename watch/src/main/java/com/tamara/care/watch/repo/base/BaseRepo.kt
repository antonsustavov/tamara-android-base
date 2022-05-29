package com.tamara.care.watch.repo.base


import com.tamara.care.watch.data.model.ErrorModel
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.utils.getErrorMessage
import retrofit2.Response

abstract class BaseRepo {

    fun <T> handleError(response: Response<*>? = null): ModelState<T> {
        return ModelState.Error(
            ErrorModel(
                response?.errorBody()?.string()?.getErrorMessage()
                    ?: ErrorModel.SOMETHING_WENT_WRONG
            )
        )
    }
}
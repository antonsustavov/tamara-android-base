package com.tamara.care.watch.data.model


sealed class ModelState<out T> {

    data class Success<T>(val data: T) : ModelState<T>()

    data class Error(val error: ErrorModel) : ModelState<Nothing>()

    object Loading : ModelState<Nothing>()

}


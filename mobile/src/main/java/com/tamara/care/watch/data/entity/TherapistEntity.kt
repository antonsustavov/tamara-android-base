package com.tamara.care.watch.data.entity

data class TherapistEntity(
    var name: String? = null,
    var image: String? = null,
    var time: List<String>? = null,
    var languages: MutableList<String>? = null,
)
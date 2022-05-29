package com.tamara.care.watch.utils

import androidx.annotation.StringDef

const val MDY = "MM.dd.yyyy"
const val DMY = "dd.MM.yyyy"

const val DM_HH_MM = "dd MMM hh:mm"//for example 15 Aug 08:48

const val HM = "hh:mm"
const val HMS = "hh:mm:ss"
const val HM_A = "hh:mm a"

const val MDY_HM_A = "MM.dd.yyyy hh:mm a"
const val TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

@StringDef(MDY, DMY, MDY_HM_A, HM, TIMESTAMP)
@Retention(AnnotationRetention.SOURCE)
annotation class DateFormat

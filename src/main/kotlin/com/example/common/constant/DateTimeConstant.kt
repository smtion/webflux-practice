package com.example.common.constant

import java.time.format.DateTimeFormatter

const val DATE_FORMAT = "yyyy-MM-dd"
const val LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val DATE_TIME_IN_MINUTES_FORMAT = "yyyy-MM-dd HH:mm"

const val DATE_MATH_NOW_M = "now/m"

val LOCAL_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)

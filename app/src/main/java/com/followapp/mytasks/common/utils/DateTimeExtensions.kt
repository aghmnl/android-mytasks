package com.followapp.mytasks.common.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

// https://stackoverflow.com/questions/63929730/materialdatepicker-returning-wrong-value
fun Long.toLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

// https://stackoverflow.com/questions/63929730/materialdatepicker-returning-wrong-value
fun Long.toUTCLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.ofOffset("UTC", ZoneOffset.UTC))
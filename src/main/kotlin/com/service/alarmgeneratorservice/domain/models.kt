package com.service.alarmgeneratorservice.domain

import java.time.LocalDateTime

data class AlarmMessage(
    val alarmType: AlarmType,
    val addressAlarm: AddressAlarm,
    val timestamp: LocalDateTime
)

enum class AlarmType(
    val value: Int
) {
    FIRE(0),
    SMOKE(2),
    WATER(3)
}

data class AddressAlarm(
    val street: String,
    val house: Short
)
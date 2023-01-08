package com.service.alarmgeneratorservice.domain

import java.time.LocalDateTime

data class AlarmMessage(
    val isSmoke: Boolean,
    val isFire: Boolean,
    val isWater: Boolean,
    val addressAlarm: AddressAlarm,
    val timestamp: LocalDateTime
) {
    val length = 13
}

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
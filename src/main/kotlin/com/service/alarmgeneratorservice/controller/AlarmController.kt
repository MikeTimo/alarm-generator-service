package com.service.alarmgeneratorservice.controller

import com.service.alarmgeneratorservice.codec.BitBufferCodec
import com.service.alarmgeneratorservice.domain.AddressAlarm
import com.service.alarmgeneratorservice.domain.AlarmMessage
import com.service.alarmgeneratorservice.domain.AlarmType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class AlarmController(
    private val codec: BitBufferCodec
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    var street: String = ""
    var house: Short = 0
    var alarmType = AlarmType.FIRE

    var isSmoke = false
    var isFire = false
    var isWater = false

    @Synchronized
    fun sendState(): Boolean {
        val alarmMessage = createAlarmMessage()
        if (!validMessage(alarmMessage)) {
            logger.error("not valid message")
            return false
        }
        val message = codec.encodeMessage(pakAppMessage)
        logger.warn("Send message $alarmMessage")
        outboundDataSink.next(message)
        return true
    }

    @Synchronized
    fun changeActivity(type: AlarmType, state: Boolean) {
        when (type) {
            AlarmType.SMOKE -> {
                isSmoke = state
            }
            AlarmType.FIRE -> {
                isFire = state
            }
            AlarmType.WATER -> {
                isWater = state
            }
        }
    }

    private fun createAlarmMessage(): AlarmMessage {
        return AlarmMessage(
            alarmType = alarmType,
            addressAlarm = AddressAlarm(street, house),
            timestamp = LocalDateTime.now()
        )
    }

    private fun validMessage(alarmMessage: AlarmMessage): Boolean {
        var valid = true
        if (alarmMessage.addressAlarm.house == 0.toShort()
            || alarmMessage.addressAlarm.street.isEmpty()
        ) valid = false
        return valid
    }
}
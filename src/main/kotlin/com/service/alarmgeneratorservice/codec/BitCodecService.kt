package com.service.alarmgeneratorservice.codec

import com.service.alarmgeneratorservice.domain.AlarmMessage

class BitCodecService {

    companion object {
        const val BITS_IN_BYTES = 8
    }

    fun encodeMessage(alarmMessage: AlarmMessage): ByteArray {
        val buffer = BitCodec.allocate(alarmMessage.length * BITS_IN_BYTES)
        buffer.add(alarmMessage.isSmoke, 1 * BITS_IN_BYTES)
        buffer.add(alarmMessage.isFire, 1 * BITS_IN_BYTES)
        buffer.add(alarmMessage.isWater, 1 * BITS_IN_BYTES)
        buffer.add(alarmMessage.addressAlarm.street, 8 * BITS_IN_BYTES)
        buffer.add(alarmMessage.addressAlarm.house, 2 * BITS_IN_BYTES)
        buffer.add(alarmMessage.timestamp, 8 * BITS_IN_BYTES)
        return buffer.getByteArray()
    }
}
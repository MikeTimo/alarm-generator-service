package com.service.alarmgeneratorservice.codec

class BitCodec private constructor(bitLength: Int) {
    private var words: LongArray = LongArray(0)
    var length = bitLength
    private var position = 0

    init {
        words = LongArray((length + 63) / 64)
        position = 0
    }

    companion object {
        fun allocate(bitLength: Int): BitCodec {
            return BitCodec(bitLength)
        }
    }

    fun getByteArray(): ByteArray {
        var position = 0
        val bytes = ByteArray((this.length + 7) / 8)
        if (this.length != 0) {
            for (i in bytes.indices) {
                bytes[i] =
                    this.getByte(position, if (i == bytes.size - 1 && this.length % 8 != 0) this.length % 8 else 8)
                position += 8
            }
        }
        return bytes
    }

    fun add(any: Any, length: Int) {
        when {
            any is Boolean -> {
                this.add(if (any) 1L else 0L, 1)
            }
            any is Short -> {

            }

        }
    }

    private fun add(value: Long, length: Int) {
        val wordIndex: Int = this.position / 64
        val wordPosition: Int = this.position % 64
        var longValue = value shl 64 - length
        if (wordPosition == 0) {
            this.words[wordIndex] = longValue
        } else {
            val var10000: LongArray
            if (64 - wordPosition >= length) {
                longValue = longValue ushr wordPosition
                var10000 = this.words
                var10000[wordIndex] = var10000[wordIndex] or longValue
            } else {
                val first = longValue ushr wordPosition
                val second = longValue shl 64 - wordPosition
                var10000 = this.words
                var10000[wordIndex] = var10000[wordIndex] or first
                this.words[wordIndex + 1] = second
            }
        }
        this.position += length
    }

    private fun getByte(position: Int, length: Int): Byte {
        return this.getLong(position, length).toInt().toByte()
    }

    private fun getLong(position: Int, length: Int): Long {
        val wordIndex = position / 64
        val wordPosition = position % 64
        var result: Long
        if (wordPosition == 0) {
            result = this.words[wordIndex]
        } else {
            result = this.words[wordIndex] shl wordPosition
            if (wordPosition > 64 - length) {
                result = result or (this.words[wordIndex + 1] ushr 64 - wordPosition)
            }
        }
        return result ushr 64 - length
    }
}
package com.service.alarmgeneratorservice.udp.config

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Encoder : MessageToByteEncoder<ByteArrayMessage>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun encode(ctx: ChannelHandlerContext, msg: ByteArrayMessage?, buffer: ByteBuf) {
        try {
            val buffer: ByteBuf = ctx.channel().alloc().buffer((msg as ByteArray).size)
            buffer.writeBytes(msg as ByteArray)
        } catch (e: Exception) {
            logger.error("error at time of encoding=$e")
        }
    }
}
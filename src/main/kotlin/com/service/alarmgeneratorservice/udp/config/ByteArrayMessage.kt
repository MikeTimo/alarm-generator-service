package com.service.alarmgeneratorservice.udp.config

import io.netty.channel.DefaultAddressedEnvelope
import java.net.InetSocketAddress

class ByteArrayMessage(
    val byte: ByteArray,
    val recipient: InetSocketAddress,
    val sender: InetSocketAddress
) : DefaultAddressedEnvelope<ByteArray, InetSocketAddress>(byte, recipient, sender)
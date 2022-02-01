package com.hdhgbot.analyticsbot.messaging.data

import javax.management.monitor.StringMonitor

data class ChatMailingModel(
    val mailingId: String = "",
    val chatId: Long = 0L,
    val messageId: Int = 0
)
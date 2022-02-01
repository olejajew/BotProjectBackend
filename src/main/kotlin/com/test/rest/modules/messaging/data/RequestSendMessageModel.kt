package com.test.rest.modules.messaging.data

import com.hdhgbot.analyticsbot.messaging.data.UrlButtonModel

data class RequestSendMessageModel(
    val message: String,
    val images: List<String>?,
    val buttons: List<UrlButtonModel>?,
    val id: String? = null,
    val sendTime: Long? = null
)
package com.test.rest.modules.messaging.data

import com.hdhgbot.analyticsbot.messaging.data.UrlButtonModel

data class RequestUpdateMessageModel(
    val message: String,
    val buttons: List<UrlButtonModel>?,
    val newImages: List<String>?,
    val id: String,
    val sendTime: Long = System.currentTimeMillis()
)
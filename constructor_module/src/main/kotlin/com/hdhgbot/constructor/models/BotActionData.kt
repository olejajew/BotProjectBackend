package com.hdhgbot.constructor.models

data class BotActionData(
    val text: String?,
    val images: List<String>?,
    val buttons: List<List<String>>?,
    val scriptId: String?
)
package com.test.rest.modules.general.data

import java.util.*

data class CreateBotModel(
    val botToken: String,
    val botUserName: String,
    val description: String,
    val name: String,
    val botId: String = UUID.randomUUID().toString()
)
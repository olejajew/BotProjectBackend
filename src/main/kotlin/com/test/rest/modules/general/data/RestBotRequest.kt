package com.test.rest.modules.general.data

import org.json.JSONObject

data class RestBotRequest<T>(
    val botId: String,
    val data: T
)
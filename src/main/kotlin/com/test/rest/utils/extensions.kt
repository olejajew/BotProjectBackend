package com.test.rest.utils

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.test.rest.modules.general.data.RestBotRequest
import com.test.rest.modules.referrers.RestReferrersProvider
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

suspend fun ApplicationCall.didNotFindBot() {
    respond(HttpStatusCode.BadRequest, "Has not bot_id or didn't find target bot")
}

suspend fun ApplicationCall.hasNotModule(moduleName: String) {
    respond(HttpStatusCode.BadGateway, "Has not $moduleName module")
}

suspend inline fun <reified T> ApplicationCall.receiveBotRequest(): RestBotRequest<T> {
    val gson = Gson()
    val model = receive<RestBotRequest<String>>()
    val dataString = gson.toJson(model.data)
    return RestBotRequest(
        model.botId,
        gson.fromJson(dataString, T::class.java)
    )
}
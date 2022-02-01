package com.test.rest.modules.general

import com.test.rest.BotsProvider
import com.test.rest.models.BotInfoModel
import com.test.rest.modules.general.data.CreateBotModel
import com.test.rest.utils.didNotFindBot
import com.test.rest.utils.receiveBotRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

object MainInfoProvider {

    suspend fun getBotsList(call: ApplicationCall) {
        call.respond(BotsProvider.getBotsInfo())
    }

    suspend fun updateBotInfo(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<BotInfoModel>()
        if (!BotsProvider.checkHasBot(botRequest.botId)) {
            call.didNotFindBot()
            return
        }
        BotsProvider.updateBotInfo(botRequest.botId, botRequest.data)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun createBot(call: ApplicationCall) {
        val createBotModel = call.receive<CreateBotModel>()
        BotsProvider.createBot(createBotModel)
        call.respond(HttpStatusCode.OK)
    }


}
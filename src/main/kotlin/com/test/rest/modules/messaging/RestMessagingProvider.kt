package com.test.rest.modules.messaging

import com.hdhgbot.analyticsbot.messaging.data.PostSendMessageModel
import com.hdhgbot.analyticsbot.messaging.data.UpdateMessageModel
import com.test.rest.BotsProvider
import com.test.rest.modules.messaging.data.RequestSendMessageModel
import com.test.rest.modules.messaging.data.RequestUpdateMessageModel
import com.test.rest.utils.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

object RestMessagingProvider {

    suspend fun sendMailing(call: ApplicationCall) {
        //todo Вот тут мы картинки ловим в base64. Неверно. Надо ловить их через спец метод
        try {
            val botRequest = call.receiveBotRequest<RequestSendMessageModel>()
            if (!BotsProvider.checkHasBot(botRequest.botId)) {
                call.didNotFindBot()
                return
            }
            BotsProvider.sendMessage(
                PostSendMessageModel(
                    botRequest.botId,
                    botRequest.data.message,
                    botRequest.data.images,
                    botRequest.data.buttons
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        call.respond(HttpStatusCode.OK)
    }

    suspend fun getMailingList(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        if (!BotsProvider.checkHasBot(botId)) {
            call.didNotFindBot()
            return
        }
        try {
            val mailing = BotsProvider.getBot(botId)?.getAnalyticsModule()?.getAllMailing()
            if (mailing == null) {
                call.hasNotModule("messaging")
                return
            }
            call.respond(mailing)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun deleteMailing(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        if (!BotsProvider.checkHasBot(botId)) {
            call.didNotFindBot()
            return
        }
        val mailingId = call.parameters[RestParameters.MAILING_ID]!!
        BotsProvider.getBot(botId).getAnalyticsModule().deleteMessage(mailingId)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun updateMessage(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<RequestUpdateMessageModel>()
        if (!BotsProvider.checkHasBot(botRequest.botId)) {
            call.didNotFindBot()
            return
        }
        BotsProvider.getBot(botRequest.botId).getAnalyticsModule().updateMessage(
            UpdateMessageModel(
                botRequest.botId,
                botRequest.data.message,
                botRequest.data.newImages,
                botRequest.data.buttons,
                botRequest.data.id
            )
        )
        call.respond(HttpStatusCode.OK)
    }

    suspend fun savePlanningMailing(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<RequestSendMessageModel>()
        if (!BotsProvider.checkHasBot(botRequest.botId)) {
            call.didNotFindBot()
            return
        }
        if (botRequest.data.sendTime ?: 0 < System.currentTimeMillis()) {
            call.respond(HttpStatusCode.BadRequest, "Send time must be less then current time")
            return
        }
        BotsProvider.getBot(botRequest.botId).getAnalyticsModule().savePlanningMessage(
            PostSendMessageModel(
                botRequest.botId,
                botRequest.data.message,
                botRequest.data.images,
                botRequest.data.buttons,
                botRequest.data.sendTime
            )
        )
        call.respond(HttpStatusCode.OK)
    }

    suspend fun getPlanningMailing(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        if (!BotsProvider.checkHasBot(botId)) {
            call.didNotFindBot()
            return
        }
        try {
            val mailing = BotsProvider.getBot(botId).getAnalyticsModule().getPlanningMailings()
            call.respond(mailing)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun deletePlanningMailing(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        if (!BotsProvider.checkHasBot(botId)) {
            call.didNotFindBot()
            return
        }
        val mailingId = call.parameters[RestParameters.MAILING_ID]!!
        BotsProvider.getBot(botId).getAnalyticsModule().deletePlanningMessage(mailingId)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun updatePlanningMailing(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<RequestUpdateMessageModel>()
        if (!BotsProvider.checkHasBot(botRequest.botId)) {
            call.didNotFindBot()
            return
        }
        BotsProvider.getBot(botRequest.botId).getAnalyticsModule().updatePlanningMessage(
            UpdateMessageModel(
                botRequest.botId,
                botRequest.data.message,
                botRequest.data.newImages,
                botRequest.data.buttons,
                botRequest.data.id,
                botRequest.data.sendTime
            )
        )
        call.respond(HttpStatusCode.OK)
    }

}
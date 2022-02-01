package com.test.rest.modules.referrers

import com.hdhgbot.analyticsbot.referrer.data.ReferrerLinkModel
import com.hdhgbot.analyticsbot.referrer.data.UserReferrerInfo
import com.test.rest.BotsProvider
import com.test.rest.modules.referrers.data.RespondReferrerInfoModel
import com.test.rest.modules.referrers.data.RestReferrersInfoModel
import com.test.rest.utils.*
import com.test.rest.utils.RestParameters
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

object RestReferrersProvider {

    suspend fun getReferrersList(call: ApplicationCall) {
        if (!checkHasBotInfo(call)) {
            call.didNotFindBot()
            return
        }
        val getAnalyticsBot = BotsProvider.getBot(call.parameters[RestParameters.BOT_ID]!!)
        val referrerLinks = getAnalyticsBot.getAnalyticsModule().getAllReferrerLinks().map {
            RespondReferrerInfoModel(it, getAnalyticsBot.getChatBot().botUsername)
        }
        call.respond(referrerLinks)
    }

    suspend fun addReferrerLinks(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<ReferrerLinkModel>()
        if (!BotsProvider.checkHasBot(botRequest.botId)) {
            call.didNotFindBot()
            return
        }
        BotsProvider.getBot(botRequest.botId).getAnalyticsModule().saveReferrerLink(botRequest.data)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun deleteReferrerLink(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        val referrerLinkId = call.parameters[RestParameters.REFERRER_LINK_ID]!!
        BotsProvider.getBot(botId).getAnalyticsModule().deleteReferrerLink(referrerLinkId)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun updateReferrerLink(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<ReferrerLinkModel>()
        if (!BotsProvider.checkHasBot(botRequest.botId)) {
            call.didNotFindBot()
            return
        }
        BotsProvider.getBot(botRequest.botId).getAnalyticsModule().updateReferrerLink(botRequest.data)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun getReferrersInfo(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        if (!BotsProvider.checkHasBot(botId)) {
            call.didNotFindBot()
            return
        }
        val analyticsModule = BotsProvider.getBot(botId).getAnalyticsModule()
        val bot = BotsProvider.getBot(botId)
        val referrersInfoModel = RestReferrersInfoModel(
            UserReferrerInfo(analyticsModule.getUserReferrerReward(), analyticsModule.getUserReferralReward()),
            analyticsModule.getAllReferrerLinks().map {
                RespondReferrerInfoModel(it, bot.getChatBot().botUsername)
            }
        )
        call.respond(referrersInfoModel)
    }

    suspend fun updateUserReferrerSettings(call: ApplicationCall) {
        val botRequest = call.receiveBotRequest<UserReferrerInfo>()
        BotsProvider.getBot(botRequest.botId).getAnalyticsModule().apply {
            this.updateUserReferrerInfo(botRequest.data)
        }

        call.respond(HttpStatusCode.OK)
    }

    private fun checkHasBotInfo(call: ApplicationCall): Boolean {
        val botId = call.parameters[RestParameters.BOT_ID]
        return botId != null && BotsProvider.checkHasBot(botId)
    }

}
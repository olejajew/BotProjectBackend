package com.test.rest.modules.analytics

import com.test.rest.BotsProvider
import com.test.rest.modules.analytics.models.ResponseAnalyticsModel
import com.test.rest.utils.RestParameters
import com.test.rest.utils.didNotFindBot
import io.ktor.application.*
import io.ktor.response.*
import java.util.*

object AnalyticsProvider {

    suspend fun getUsersAnalytics(call: ApplicationCall) {
        val botId = call.parameters[RestParameters.BOT_ID]!!
        if (!BotsProvider.checkHasBot(botId)) {
            call.didNotFindBot()
            return
        }
        val from = call.parameters[RestParameters.ANALYTICS_FROM]!!.toLong()
        val to = call.parameters[RestParameters.ANALYTICS_TO]?.toLongOrNull() ?: System.currentTimeMillis()
        val step = call.parameters[RestParameters.ANALYTICS_STEP]?.toIntOrNull() ?: 1
        call.respond(
            BotsProvider.getBot(botId)!!
                .getAnalyticsModule()
                .getUsersAnalytics(from, to, step)
                .map {
                    ResponseAnalyticsModel(
                        Calendar.getInstance().apply { time = Date(it.data) }.toInstant().toString(),
                        it.usersCountModel.totalUsers,
                        it.usersCountModel.aliveUsers,
                        it.usersCountModel.referrerUsers
                    )
                }
        )
    }

}
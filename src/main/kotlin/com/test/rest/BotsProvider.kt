package com.test.rest

import com.hdhgbot.analyticsbot.messaging.data.PostSendMessageModel
import com.hdhgbot.analyticsbot.users.data.UsersCountModel
import com.hdhgbot.constructor.models.InstructionsModel
import com.test.rest.database.DatabaseProvider
import com.test.rest.models.BotInfoModel
import com.test.rest.models.BotModel
import com.test.rest.modules.general.data.CreateBotModel

object BotsProvider {

    private val bots = hashMapOf<String, UserChatBot>()

    fun init() {
        DatabaseProvider.getBotsList().forEach {
            println(it)
            initBot(
                it.botToken,
                it.botUserName,
                it.botId
            )
        }
    }

    private fun initBot(botToken: String, botUserName: String, botId: String) {
        println("Init bot = $botUserName, $botId")
        bots[botId] = UserChatBot(
            botToken,
            botUserName, botId
        )
        bots[botId]!!.runBot {  }
    }

    fun checkHasBot(botId: String): Boolean {
        return bots.containsKey(botId)
    }

    fun sendMessage(sendMessageModel: PostSendMessageModel) {
        getBot(sendMessageModel.botId)
            .getAnalyticsModule()
            .sendMessageToEverything(sendMessageModel)
    }

    fun getBot(botId: String): UserChatBot {
        return bots[botId]!!
    }

    fun getBotInstructions(botId: String): String? {
        if(!checkHasBot(botId)){
            return null
        }
        return getBot(botId).getConstructorModule().getInstructionsString()
    }

    fun updateBotInstructions(botId: String, instructions: InstructionsModel) {
        getBot(botId).updateInstructions(instructions)
    }

    fun updateBotInfo(botId: String, data: BotInfoModel) {
        DatabaseProvider.updateBotInfo(botId, data)
    }

    fun createBot(createBotModel: CreateBotModel) {
        val id = DatabaseProvider.createBot(createBotModel)
        if (id != null) {
            initBot(
                createBotModel.botToken,
                createBotModel.botUserName,
                id
            )
        }

    }

    fun getBotsInfo(): List<BotModel> {
        DatabaseProvider.getBotsList().map {
            BotModel(
                it.botId,
                "https://t.me/${it.botUserName}",
                it.botName,
                it.botDescription,
                UsersCountModel(0, 0, 0)
            )
        }
    }

}
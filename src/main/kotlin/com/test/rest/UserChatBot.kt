package com.test.rest

import com.doblack.bot_library.analytics.AnalyticsBot
import com.doblack.bot_library.analytics.AnalyticsModule
import com.doblack.bot_library.analytics.messaging.data.PostSendMessageModel
import com.doblack.bot_library.analytics.referrer.NewReferralListener
import com.doblack.bot_library.base.ChatBot
import com.doblack.bot_library.base.chatId
import com.doblack.bot_library.base.getCommand
import com.doblack.bot_library.constructor.BotConstructor
import com.doblack.bot_library.constructor.ConstructorModule
import com.doblack.bot_library.constructor.models.InstructionsModel
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

class UserChatBot(
    private val botToken: String,
    private val botUserName: String,
    private val botId: String,
) : ChatBot(), AnalyticsBot, BotConstructor, NewReferralListener {

    //todo Бля буду конфликт между mailin со стороны analytics и со стороны constructor
    //todo Вынести строки для сообщений Reward и Referrer в отдельное место в настройках

    private val analyticsModule = AnalyticsModule(this)
    private val constructorModule = ConstructorModule(this)

    override fun documentReceived(message: Message) {

    }

    override fun photoReceived(message: Message) {

    }

    override fun commandReceived(message: Message) {
        super.commandReceived(message)
        constructorModule.onCommand(message.getCommand(), message.chatId)

    }

    override fun messageReceived(message: Message) {
        constructorModule.onMessage(message.text, message.chatId)

        when (message.text) {
            "send" -> {
                analyticsModule.sendMessageToEverything(
                    PostSendMessageModel(
                    botId,
                    "test",
                    null,
                    null
                )
                )
            }
            "user" -> {
                analyticsModule.getDatabaseHelper().chatMailingTableProvider.removeMessageByUser(message.chatId)
            }
        }
    }

    override fun callbackMessageReceived(update: Update) {
        constructorModule.callbackReceived(
            update.callbackQuery.data,
            update.callbackQuery.inlineMessageId,
            update.chatId()
        )
    }

    override fun getBotToken() = botToken

    override fun getBotUsername() = botUserName

    override fun isDebug(): Boolean {
        return false
    }

    override fun getChatBot(): ChatBot {
        return this
    }

    override fun getConstructorModule(): ConstructorModule {
        return constructorModule
    }

    override fun updateInstructions(instructionsModel: InstructionsModel) {
        constructorModule.updateInstructions(instructionsModel)
    }

    override fun getBotId(): String {
        return botId
    }

    override fun getNewReferrerListener(): NewReferralListener {
        return this
    }

    override fun getAnalyticsModule(): AnalyticsModule {
        return analyticsModule
    }

    override fun referralReward(userId: Long, reward: String?) {

    }

    override fun referrerReward(userId: Long?, reward: String?) {

    }


}
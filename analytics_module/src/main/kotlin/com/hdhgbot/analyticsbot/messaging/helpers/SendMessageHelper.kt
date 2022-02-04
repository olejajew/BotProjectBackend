package com.hdhgbot.analyticsbot.messaging.helpers

import com.hdhgbot.analyticsbot.AnalyticsModule
import com.hdhgbot.analyticsbot.messaging.data.ChatMailingModel
import com.hdhgbot.analyticsbot.messaging.data.MailingModel
import com.hdhgbot.analyticsbot.messaging.data.UrlButtonModel
import com.hdhgbot.analyticsbot.utils.ListUtils
import com.hdhgbot.botlibrary.BotButton
import com.hdhgbot.botlibrary.models.ImageInputStream

class SendMessageHelper(private val analyticsModule: AnalyticsModule) {

    fun sendMessage(mailingModel: MailingModel) {
        if (mailingModel.mailingId.isEmpty()) {
            analyticsModule.getDatabaseHelper().mailingTableProvider.saveMailing(mailingModel)
        } else {
            mailingModel.mailingId
        }

        if (mailingModel.images.isNotEmpty()) {
            if (mailingModel.buttons.isNotEmpty()) {
                sendMessageWithImageAndButtons(mailingModel)
            } else {
                sendMessageWithImage(mailingModel)
            }
        } else if (mailingModel.buttons.isNotEmpty()) {
            sendMessageWithButtons(mailingModel)
        } else {
            sendTextMessage(mailingModel)
        }
    }

    private fun sendMessageWithImageAndButtons(mailingModel: MailingModel) {
        val chatMailingProvider = analyticsModule.getDatabaseHelper().chatMailingTableProvider
        val imageName = ListUtils.convertStringToList(mailingModel.images)[0]
        val image = analyticsModule.getFilesProvider().getImage(imageName)
        println("HERE = " + image)
        val buttonsStringList = ListUtils.convertStringToList(mailingModel.buttons)
        val buttons = buttonsStringList.map {
            val urlButtonModel = UrlButtonModel(it)
            BotButton(urlButtonModel.buttonText, "", urlButtonModel.buttonUrl)
        }
        val listButtons = arrayListOf(buttons)
        val imageInputStream = ImageInputStream(
            image,
            imageName
        )
        analyticsModule.getUsersProvider().getAliveUsers().forEach {
            val message =
                analyticsModule.getChatBot()
                    .sendMessageWithImageInputStream(imageInputStream, it, mailingModel.message, listButtons)

            if (message != null) {
                chatMailingProvider.saveMailingMessage(
                    ChatMailingModel(
                        mailingModel.mailingId,
                        it,
                        message.messageId
                    )
                )
            }
        }
    }

    private fun sendMessageWithImage(mailingModel: MailingModel) {
        val chatMailingProvider = analyticsModule.getDatabaseHelper().chatMailingTableProvider
        val imageName = ListUtils.convertStringToList(mailingModel.images)[0]
        val file = analyticsModule.getFilesProvider().getImage(imageName)
        analyticsModule.getUsersProvider().getAliveUsers().forEach {
            val imageInputStream = ImageInputStream(
                file,
                imageName
            )
            val message = analyticsModule.getChatBot().sendMessageWithImageInputStream(
                imageInputStream,
                it,
                mailingModel.message
            )
            if (message != null) {
                chatMailingProvider.saveMailingMessage(
                    ChatMailingModel(
                        mailingModel.mailingId,
                        it,
                        message.messageId
                    )
                )
            }
        }
    }

    private fun sendMessageWithButtons(mailingModel: MailingModel) {
        val chatMailingProvider = analyticsModule.getDatabaseHelper().chatMailingTableProvider
        val buttonsStringList = ListUtils.convertStringToList(mailingModel.buttons)
        val buttons = buttonsStringList.map {
            val urlButtonModel = UrlButtonModel(it)
            BotButton(urlButtonModel.buttonText, "", urlButtonModel.buttonUrl)
        }
        val listButtons = arrayListOf(buttons)
        analyticsModule.getUsersProvider().getAliveUsers().forEach {
            val message = analyticsModule.getChatBot().sendMessage(mailingModel.message, it, listButtons, true)
            if (message != null) {
                chatMailingProvider.saveMailingMessage(
                    ChatMailingModel(
                        mailingModel.mailingId,
                        it,
                        message.messageId,
                    )
                )
            }
        }
    }

    private fun sendTextMessage(mailingModel: MailingModel) {
        val chatMailingProvider = analyticsModule.getDatabaseHelper().chatMailingTableProvider
        analyticsModule.getUsersProvider().getAliveUsers().forEach {
            val message = analyticsModule.getChatBot().sendMessage(mailingModel.message, it)
            if (message != null) {
                chatMailingProvider.saveMailingMessage(
                    ChatMailingModel(
                        mailingModel.mailingId,
                        it,
                        message.messageId
                    )
                )
            }
        }
    }

}
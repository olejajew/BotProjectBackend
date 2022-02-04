package com.hdhgbot.constructor

import com.botlibrary.core.AwsProvider
import com.hdhgbot.botlibrary.BotButton
import com.hdhgbot.botlibrary.models.ImageInputStream
import com.hdhgbot.constructor.enums.ButtonType
import com.hdhgbot.constructor.enums.ScriptCombinations
import com.hdhgbot.constructor.models.BotActionData
import com.hdhgbot.constructor.models.BotScriptModel
import com.hdhgbot.constructor.models.ButtonModel
import com.hdhgbot.constructor.utils.ImageUtils
import java.io.File
import java.io.InputStream
import java.util.*

class ScriptExecutor(private val constructorModule: ConstructorModule) {

    fun execute(scriptModel: BotScriptModel, chatId: Long) {
        scriptModel.actions.forEach {
            when (ScriptCombinations.getById(it.combinationId)) {
                ScriptCombinations.text -> sendText(it.data, chatId)
                ScriptCombinations.text_inlineButton -> sendTextInlineButtons(it.data, chatId)
                ScriptCombinations.text_inlineButton_image -> sendTextInlineButtonsImage(it.data, chatId)
                ScriptCombinations.text_keyboardButton -> sendTextKeyboardButtons(it.data, chatId)
                ScriptCombinations.text_keyboardButton_image -> sendTextKeyboardButtonsImage(it.data, chatId)
                ScriptCombinations.text_image -> sendTextImage(it.data, chatId)
                ScriptCombinations.image -> sendImage(it.data, chatId)
                ScriptCombinations.image_inlineButton -> sendImageInlineButtons(it.data, chatId)
                ScriptCombinations.image_keyboardButton -> sendImageKeyboardButtons(it.data, chatId)
                ScriptCombinations.imageGroup -> sendImageGroup(it.data, chatId)
                ScriptCombinations.doScript -> doScript(it.data, chatId)
                null -> {
                }
            }
        }
    }

    private fun doScript(data: BotActionData, chatId: Long) {
        if (data.scriptId == null) {
            return
        }
        val script = constructorModule.getScript(data.scriptId) ?: return
        execute(script, chatId)
    }

    private fun sendImageGroup(data: BotActionData, chatId: Long) {
        if (data.images.isNullOrEmpty()) {
            return
        }
        constructorModule.chatBot.sendImagesGroupInputStream(getImages(data.images), chatId)
    }

    private fun sendImageKeyboardButtons(data: BotActionData, chatId: Long) {
        if (data.images.isNullOrEmpty() || data.buttons.isNullOrEmpty()) {
            return
        }
        val buttons = convertToBotButtons(data.buttons, ButtonType.inlineButton)
        constructorModule.chatBot.sendMessageWithImageInputStream(
            getImage(data.images.first()),
            chatId,
            buttons = buttons,
        )
    }

    private fun sendImageInlineButtons(data: BotActionData, chatId: Long) {
        if (data.images.isNullOrEmpty() || data.buttons.isNullOrEmpty()) {
            return
        }
        val buttons = convertToBotButtons(data.buttons, ButtonType.inlineButton)
        constructorModule.chatBot.sendMessageWithImageInputStream(
            getImage(data.images.first()),
            chatId,
            buttons = buttons,
        )
    }

    private fun sendImage(data: BotActionData, chatId: Long) {
        if (data.images.isNullOrEmpty()) {
            return
        }
        constructorModule.chatBot.sendMessageWithImageInputStream(getImage(data.images.first()), chatId)
    }

    private fun sendTextImage(data: BotActionData, chatId: Long) {
        if (data.text == null || data.images == null) {
            return
        }
        val imageInputStream = ImageInputStream(
            constructorModule.getFilesProvider().getImage(data.images.first()),
            data.images.first()
        )
        constructorModule.chatBot.sendMessageWithImageInputStream(imageInputStream, chatId, data.text)
    }

    private fun sendTextKeyboardButtonsImage(data: BotActionData, chatId: Long) {
        if (data.buttons == null || data.text == null || data.images.isNullOrEmpty()) {
            return
        }
        val buttons = convertToBotButtons(data.buttons, ButtonType.keyboardButton)
        //todo Не нравится, что картинки каждый раз из base64 конвертируются
        constructorModule.chatBot.sendMessageWithImageInputStream(
            getImage(data.images.first()),
            chatId,
            data.text,
            buttons,
            false
        )
    }

    private fun sendTextKeyboardButtons(data: BotActionData, chatId: Long) {
        if (data.buttons == null || data.text == null) {
            return
        }
        val buttons = convertToBotButtons(data.buttons, ButtonType.keyboardButton)
        constructorModule.chatBot.sendMessage(data.text, chatId, buttons, false)
    }

    private fun sendTextInlineButtonsImage(data: BotActionData, chatId: Long) {
        if (data.buttons == null || data.text == null || data.images.isNullOrEmpty()) {
            return
        }
        val buttons = convertToBotButtons(data.buttons, ButtonType.inlineButton)
        //todo Не нравится, что картинки каждый раз из base64 конвертируются
        constructorModule.chatBot.sendMessageWithImageInputStream(
            getImage(data.images.first()),
            chatId,
            data.text,
            buttons
        )
    }

    private fun sendTextInlineButtons(data: BotActionData, chatId: Long) {
        if (data.buttons == null || data.text == null) {
            return
        }
        val buttons = convertToBotButtons(data.buttons, ButtonType.inlineButton)
        constructorModule.chatBot.sendMessage(data.text, chatId, buttons, true)
    }

    private fun sendText(data: BotActionData, chatId: Long) {
        if (data.text == null) {
            return
        }
        constructorModule.chatBot.sendMessage(data.text, chatId)
    }

    private fun convertToBotButtons(buttons: List<List<String>>, buttonType: ButtonType): List<List<BotButton>> {
        return buttons
            .map {
                it.map {
                    val button = constructorModule.getButton(it, buttonType.id)
                    if (button?.text == null) {
                        null
                    } else {
                        BotButton(
                            button.text,
                            data = button.botScriptId.toString(),
                            url = button.link
                        )
                    }
                }.filterNotNull()
            }
    }

    private fun getImage(imageName: String): ImageInputStream {
        return ImageInputStream(constructorModule.getFilesProvider().getImage(imageName), imageName)
    }

    private fun getImages(images: List<String>): List<ImageInputStream> {
        return images.map { getImage(it) }
    }
}

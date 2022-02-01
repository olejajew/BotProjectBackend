package com.hdhgbot.constructor

import com.hdhgbot.botlibrary.ChatBot
import com.hdhgbot.constructor.models.InstructionsModel

interface BotConstructor {

    fun getConstructorModule(): ConstructorModule

    fun updateInstructions(instructionsModel: InstructionsModel)

    fun getBotId(): String

    fun getChatBot(): ChatBot

}
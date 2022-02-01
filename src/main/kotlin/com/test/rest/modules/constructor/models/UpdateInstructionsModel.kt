package com.test.rest.modules.constructor.models

import com.hdhgbot.constructor.models.InstructionsModel
import org.json.JSONObject

data class UpdateInstructionsModel(
    val botId: String,
    val instructions: InstructionsModel
)
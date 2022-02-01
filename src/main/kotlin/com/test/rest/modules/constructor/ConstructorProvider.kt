package com.test.rest.modules.constructor

import com.test.rest.BotsProvider
import com.test.rest.modules.constructor.models.ImageAddedResponse
import com.test.rest.modules.constructor.models.ImageModel
import com.test.rest.modules.constructor.models.UpdateInstructionsModel
import com.test.rest.utils.RestParameters.BOT_ID
import com.test.rest.utils.RestParameters.IMAGE_ID
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import java.lang.Exception

object ConstructorProvider {

    suspend fun getBotInstructions(call: ApplicationCall) {
        val botId = call.parameters["botId"]
        if (botId.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Need botId param")
            return
        }

        call.respond(BotsProvider.getBotInstructions(botId) ?: "{}")
    }

    suspend fun updateInstructions(call: ApplicationCall) {
        val body = call.receive<UpdateInstructionsModel>()
        try {
            BotsProvider.updateBotInstructions(body.botId, body.instructions)
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
    }

    suspend fun addNewImage(call: ApplicationCall) {
        val body = call.receive<ImageModel>()
        if (BotsProvider.checkHasBot(body.botId)) {
            val id = BotsProvider.getBot(body.botId).getConstructorModule().saveImage(body.imageBase64)
            call.respond(ImageAddedResponse(id))
        } else {
            call.respond(HttpStatusCode.BadRequest, "Hasn't bot")
        }
    }

    suspend fun getImage(call: ApplicationCall) {
        val imageId = call.parameters[IMAGE_ID] ?: call.respond(HttpStatusCode.BadRequest)
        val botId = call.parameters[BOT_ID] ?: call.respond(HttpStatusCode.BadRequest)
        if (BotsProvider.checkHasBot(botId.toString())) {
            val imageLink = BotsProvider.getBot(botId.toString()).getConstructorModule().getImageUrl(imageId.toString())
            call.respondRedirect(imageLink)
        } else {
            call.respond(HttpStatusCode.BadRequest, "Hasn't bot")
        }
    }

}
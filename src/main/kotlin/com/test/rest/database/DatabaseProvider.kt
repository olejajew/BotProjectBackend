package com.test.rest.database

import com.botlibrary.core.FirestoreProvider
import com.test.rest.models.BotInfoModel
import com.test.rest.models.BotInstanceInfoModel
import com.test.rest.modules.general.data.CreateBotModel

object DatabaseProvider {

    private const val BOTS_COLLECTION = "bots"

    fun createBot(createBotModel: CreateBotModel): String? {
        val docRef = FirestoreProvider.getCollection(BOTS_COLLECTION)
        return if (checkExistBot(createBotModel)) {
            null
        } else {
            docRef.document(createBotModel.botId).set(createBotModel)
            createBotModel.botId
        }

    }

    fun updateBotInfo(botId: String, data: BotInfoModel) {
        val docRef = FirestoreProvider.getCollection(BOTS_COLLECTION)
        docRef.document(botId).update(mapOf(
            BotInfoModel::name.name to data.name,
            BotInfoModel::description.name to data.description
        ))
    }

    fun getBotsList(): List<BotInstanceInfoModel> {
        val docRef = FirestoreProvider.getCollection(BOTS_COLLECTION)
        return docRef.get().get().toObjects(BotInstanceInfoModel::class.java)
    }

    private fun checkExistBot(createBotModel: CreateBotModel): Boolean {
        val collection = FirestoreProvider.getCollection(BOTS_COLLECTION)
        return collection.whereEqualTo(CreateBotModel::botUserName.name, createBotModel.botUserName)
            .whereEqualTo(CreateBotModel::botToken.name, createBotModel.botToken)
            .limit(1)
            .get()
            .get()
            .size() > 0
    }
}

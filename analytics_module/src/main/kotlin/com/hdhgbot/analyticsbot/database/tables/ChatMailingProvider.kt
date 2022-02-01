package com.hdhgbot.analyticsbot.database.tables

import com.google.cloud.firestore.CollectionReference
import com.hdhgbot.analyticsbot.messaging.data.ChatMailingModel

class ChatMailingProvider(private val subCollection: CollectionReference) {

    fun saveMailingMessage(chatMailingModel: ChatMailingModel) {
        subCollection.document(chatMailingModel.mailingId)
            .set(chatMailingModel)
    }

    fun removeMessageByUser(chatId: Long) {
        subCollection.document(chatId.toString())
            .delete()
    }

    fun getChatMailingsId(mailingId: String): List<ChatMailingModel> {
        return subCollection.whereEqualTo(ChatMailingModel::mailingId.name, mailingId)
            .get()
            .get()
            .toObjects(ChatMailingModel::class.java)
    }

    fun deleteMailing(mailingId: String) {
        subCollection
            .whereEqualTo(ChatMailingModel::mailingId.name, mailingId)
            .get()
            .get()
            .forEach {
                it.reference.delete()
            }
    }

}
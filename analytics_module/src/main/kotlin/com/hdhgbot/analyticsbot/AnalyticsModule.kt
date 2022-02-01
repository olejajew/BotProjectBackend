package com.hdhgbot.analyticsbot

import com.hdhgbot.analyticsbot.database.DatabaseHelper
import com.hdhgbot.analyticsbot.files.FilesProvider
import com.hdhgbot.analyticsbot.messaging.MessagingProvider
import com.hdhgbot.analyticsbot.messaging.data.PostSendMessageModel
import com.hdhgbot.analyticsbot.messaging.data.UpdateMessageModel
import com.hdhgbot.analyticsbot.referrer.ReferrerProvider
import com.hdhgbot.analyticsbot.referrer.data.ReferrerLinkModel
import com.hdhgbot.analyticsbot.referrer.data.UserReferrerInfo
import com.hdhgbot.analyticsbot.users.UsersProvider
import com.hdhgbot.analyticsbot.users.data.UsersCountModel

class AnalyticsModule(
    private val analyticsBot: AnalyticsBot
) {

    private val databaseHelper = DatabaseHelper(analyticsBot.getBotId(), analyticsBot.isDebug())
    private val filesProvider = FilesProvider(this)
    private val messagingProvider = MessagingProvider(this)
    private val referrerProvider = ReferrerProvider(this, analyticsBot.getNewReferrerListener())
    private val usersProvider = UsersProvider(this)

    fun getDatabaseHelper() = databaseHelper
    fun getFilesProvider() = filesProvider
    fun getMessagingProvider() = messagingProvider
    fun getReferrerProvider() = referrerProvider
    fun getUsersProvider() = usersProvider
    fun getBotId() = analyticsBot.getBotId()
    fun getChatBot() = analyticsBot.getChatBot()

    init {
        messagingProvider.init()
        referrerProvider.init(true)
        usersProvider.init()
        analyticsBot.getChatBot().addUserLifecycleObserver(usersProvider)
        analyticsBot.getChatBot().addUserLifecycleObserver(referrerProvider)
    }

    fun sendMessageToEverything(postSendMessageModel: PostSendMessageModel) {
        messagingProvider.sendMessageToEverything(postSendMessageModel)
    }

    fun getUserCount(): UsersCountModel {
        return usersProvider.getUsersCount()
    }

    fun getUsersAnalytics(from: Long, to: Long, step: Int) = usersProvider.getUsersAnalytics(from, to, step)
    fun getAllMailing() = messagingProvider.getAllMailing()
    fun deleteMessage(mailingId: String) {
        messagingProvider.deleteMessage(mailingId)
    }

    fun updateMessage(updateMessageModel: UpdateMessageModel) {
        messagingProvider.updateMessage(updateMessageModel)
    }

    fun savePlanningMessage(sendMessageModel: PostSendMessageModel) {
        messagingProvider.savePlanningMessage(sendMessageModel)
    }

    fun getPlanningMailings() = messagingProvider.getPlanningMailings()
    fun deletePlanningMessage(mailingId: String) {
        messagingProvider.deletePlanningMessage(mailingId)
    }

    fun updatePlanningMessage(updateMessageModel: UpdateMessageModel) {
        messagingProvider.updatePlanningMessage(updateMessageModel)
    }

    fun getAllReferrerLinks() = referrerProvider.getAllReferrerLinks()

    fun saveReferrerLink(data: ReferrerLinkModel) {
        referrerProvider.saveReferrerLink(data)
    }

    fun deleteReferrerLink(referrerLinkId: String) {
        referrerProvider.deleteReferrerLink(referrerLinkId)
    }

    fun updateReferrerLink(data: ReferrerLinkModel) {
        referrerProvider.updateReferrerLink(data)
    }

    fun getUserReferrerReward(): String? {
        return referrerProvider.getUserReferrerReward()
    }

    fun getUserReferralReward(): String? {
        return referrerProvider.getUserReferralReward()
    }

    fun updateUserReferrerInfo(userReferrerInfo: UserReferrerInfo) {
        referrerProvider.updateUserReferrerInfo(userReferrerInfo)
    }

}
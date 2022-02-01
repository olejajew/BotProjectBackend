package com.hdhgbot.analyticsbot

import com.hdhgbot.analyticsbot.referrer.NewReferralListener
import com.hdhgbot.botlibrary.ChatBot

interface AnalyticsBot {

    fun isDebug(): Boolean

    fun getChatBot(): ChatBot

    fun getBotId(): String

    fun getNewReferrerListener(): NewReferralListener

    fun getAnalyticsModule(): AnalyticsModule

}
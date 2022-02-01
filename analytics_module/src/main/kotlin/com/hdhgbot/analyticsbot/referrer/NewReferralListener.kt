package com.hdhgbot.analyticsbot.referrer

import com.hdhgbot.analyticsbot.referrer.data.ReferrerPair

interface NewReferralListener {

    fun referralReward(userId: Long, reward: String?)

    fun referrerReward(userId: Long?, reward: String?)

}
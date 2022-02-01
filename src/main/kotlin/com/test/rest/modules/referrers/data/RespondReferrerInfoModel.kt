package com.test.rest.modules.referrers.data

import com.hdhgbot.analyticsbot.AnalyticsModule
import com.hdhgbot.analyticsbot.referrer.data.ReferrerLinkModel

class RespondReferrerInfoModel(referrerLinkModel: ReferrerLinkModel, botUsername: String) {

    var referrerName: String
    var referrer: String
    var limit: Int
    var reward: String
    var referrerId: String
    var usedCount: Int
    var link: String

    init {
        referrerName = referrerLinkModel.referrerName
        referrer = referrerLinkModel.referrer
        limit = referrerLinkModel.limit
        reward = referrerLinkModel.reward
        referrerId = referrerLinkModel.referrerId
        usedCount = referrerLinkModel.usedCount
        link = "https://t.me/${botUsername}?start=$referrer"
    }

}
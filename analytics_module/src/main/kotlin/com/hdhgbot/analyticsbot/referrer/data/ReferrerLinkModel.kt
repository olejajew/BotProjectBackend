package com.hdhgbot.analyticsbot.referrer.data

import java.util.*

data class ReferrerLinkModel(
    val referrerName: String = "",
    val referrer: String = "",
    val limit: Int = 0,
    val reward: String = "",
    var referrerId: String = UUID.randomUUID().toString(),
    val usedCount: Int = 0
)
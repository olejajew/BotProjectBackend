package com.test.rest.modules.referrers.data

import com.hdhgbot.analyticsbot.referrer.data.UserReferrerInfo

data class RestReferrersInfoModel(
    val userReferrerInfo: UserReferrerInfo,
    val referrerLinks: List<RespondReferrerInfoModel>
)

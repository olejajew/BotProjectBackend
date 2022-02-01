package com.hdhgbot.analyticsbot.database.models

import com.hdhgbot.analyticsbot.users.data.User

data class UsersAnalyticsDatabaseModel(
    val total: List<User>,
    val alive: List<User>,
    val referrers: List<Long>
)

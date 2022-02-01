package com.test.rest.models

import com.hdhgbot.analyticsbot.users.data.UsersCountModel

data class BotModel(
    val id: String,
    val link: String,
    val botName: String,
    val botDescription: String,
    val usersInfo: UsersCountModel
)
package com.hdhgbot.analyticsbot.users.data

data class User(
    val tgUserId: Long = 0L,
    val createdTime: Long = 0L,
    val alive: Boolean = true,
    val blockedTime: Long? = null,
)
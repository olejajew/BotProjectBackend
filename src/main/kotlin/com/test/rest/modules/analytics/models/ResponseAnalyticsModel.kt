package com.test.rest.modules.analytics.models

data class ResponseAnalyticsModel(
    val day: String,
    val totalUsers: Int,
    val aliveUsers: Int,
    val referrerUsers: Int
)
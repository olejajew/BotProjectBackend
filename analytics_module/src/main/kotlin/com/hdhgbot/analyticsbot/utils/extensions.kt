package com.hdhgbot.analyticsbot.utils

import com.hdhgbot.analyticsbot.users.data.User
import java.util.concurrent.TimeUnit

fun List<User>.groupByDay(step: Int, timeFrom: Long, onResult: (Int, Int) -> Unit) {
    groupBy {
        (it.createdTime - timeFrom) / TimeUnit.DAYS.toMillis(step.toLong())
    }.forEach { t, u ->
        onResult(t.toInt(), u.size)
    }
}
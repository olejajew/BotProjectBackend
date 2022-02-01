package com.hdhgbot.analyticsbot.utils

object ListUtils {

    fun convertListToString(list: List<String>): String {
        return list.joinToString(LIST_ITEMS_SEPARATOR)
    }

    fun convertStringToList(string: String): List<String> {
        return string.split(LIST_ITEMS_SEPARATOR)
            .filter { it.isNotEmpty() }
    }

}
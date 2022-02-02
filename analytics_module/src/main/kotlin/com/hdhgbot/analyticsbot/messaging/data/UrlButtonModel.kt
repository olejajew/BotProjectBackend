package com.hdhgbot.analyticsbot.messaging.data

import com.hdhgbot.analyticsbot.utils.URL_BUTTON_SEPARATOR

class UrlButtonModel {

    lateinit var buttonText: String
    lateinit var buttonUrl: String

    constructor(buttonText: String, buttonUrl: String) {
        this.buttonText = buttonText
        this.buttonUrl = buttonUrl
    }

    constructor(string: String) {
        val items = string.split(URL_BUTTON_SEPARATOR)
        println("HERE $string. Item = $items")
        this.buttonText = items[0]
        this.buttonUrl = items[1]
    }

    override fun toString(): String {
        return "$buttonText$URL_BUTTON_SEPARATOR$buttonUrl"
    }

}

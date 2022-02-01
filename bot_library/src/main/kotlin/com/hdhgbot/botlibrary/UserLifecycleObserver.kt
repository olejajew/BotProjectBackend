package com.hdhgbot.botlibrary

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

interface UserLifecycleObserver {

    fun onStartCommand(message: Message)

    fun onUserBlocked(chatId: Long)

}
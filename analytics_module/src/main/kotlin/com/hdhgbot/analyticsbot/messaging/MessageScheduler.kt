package com.hdhgbot.analyticsbot.messaging

import com.hdhgbot.analyticsbot.messaging.data.MailingModel
import java.util.*

class MessageScheduler(private val messagingProvider: MessagingProvider) {

    private var timer: Timer? = null
    private var sendingTime: Long? = null
    private var mailingId: String? = null

    fun init() {
        sendingTime = null
        mailingId = null

        timer?.cancel()
        println("Check need scheduling")
        val nextPlanningMessage = messagingProvider.getNextScheduledMessage() ?: return
        println("Next scheduling message = $nextPlanningMessage")
        sendingTime = nextPlanningMessage.sentTime
        mailingId = nextPlanningMessage.mailingId
        timer = Timer()
        timer!!.schedule(
            object : TimerTask() {
                override fun run() {
                    println("Send scheduling message")
                    messagingProvider.sendMessage(nextPlanningMessage)
                    init()
                }
            }, nextPlanningMessage.sentTime - System.currentTimeMillis()
        )
    }

}
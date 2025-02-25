package com.nextcloud.talk.adapters.messages

import com.nextcloud.talk.models.json.chat.ChatMessage

interface SystemMessageInterface {
    fun expandSystemMessage(chatMessage: ChatMessage)
    fun collapseSystemMessages()
}

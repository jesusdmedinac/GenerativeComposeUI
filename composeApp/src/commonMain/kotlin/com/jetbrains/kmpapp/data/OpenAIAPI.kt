package com.jetbrains.kmpapp.data

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI

interface ChatAPI {
    suspend fun sendNewMessage(chatMessage: ChatMessage)
}

class OpenAIAPI(
    private val openAI: OpenAI
) : ChatAPI {
    private val messages: MutableList<ChatMessage> = mutableListOf(
        ChatMessage(
            role = ChatRole.System,
            content = """
                        You are a Generative UI assistant. You are able to support next list of compose elements:
                        - Text
                        """
        )
    )

    override suspend fun sendNewMessage(chatMessage: ChatMessage) {
        messages.add(chatMessage)
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = messages
        )
        openAI.chatCompletion(chatCompletionRequest)
            .choices
            .firstOrNull()
            ?.let { chatChoice ->
                messages.add(
                    ChatMessage(
                        role = ChatRole.System,
                        messageContent = chatMessage.messageContent
                    )
                )
            }
    }
}
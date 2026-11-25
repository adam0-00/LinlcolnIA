package org.ldv.lincoln.dto.mistral

data class MistralChatRequest(
    val model: String,
    val messages: List<MistralMessage>
)
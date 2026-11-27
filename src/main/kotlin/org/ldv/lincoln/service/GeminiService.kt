package org.ldv.lincoln.service

import org.ldv.lincoln.dto.gemini.*
import org.ldv.lincoln.model.entity.ModeleIA
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class GeminiService(
    restClientBuilder: RestClient.Builder,
    @Value("\${gemini.api.key}") private val apiKey: String
) : IAService {

    private val restClient = restClientBuilder.build()

    override fun getFournisseur(): String = "Gemini"

    override fun envoyerPrompt(prompt: String, modele: ModeleIA): String {
        val requete = GeminiChatRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(prompt))))
        )

        val reponse = restClient.post()
            .uri(modele.urlAPI)
            .header("x-goog-api-key", apiKey)
            .header("Content-Type", "application/json")
            .body(requete)
            .retrieve()
            .body(GeminiChatResponse::class.java)

        return reponse?.candidates?.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: "Aucune réponse reçue de Gemini."
    }
}
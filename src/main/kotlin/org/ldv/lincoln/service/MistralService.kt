package org.ldv.lincoln.service

import org.ldv.lincoln.dto.mistral.MistralChatRequest
import org.ldv.lincoln.dto.mistral.MistralChatResponse
import org.ldv.lincoln.dto.mistral.MistralMessage
import org.ldv.lincoln.model.entity.ModeleIA
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class MistralService(
    private val restClientBuilder: RestClient.Builder,
    @Value("\${mistral.api.key}")
    private val apiKey: String
) : IAService {

    override fun getFournisseur(): String = "Mistral"

    override fun envoyerPrompt(prompt: String, modele: ModeleIA): String {
        // Étape 1 : message de l'utilisateur
        val message = MistralMessage(role = "user", content = prompt)

        // Étape 2 : préparation de la requête
        val request = MistralChatRequest(
            model = modele.nom,
            messages = listOf(message)
        )

        // Étape 3 : envoi de la requête HTTP POST
        val client = restClientBuilder.build()
        val response = client.post()
            .uri(modele.urlAPI)
            .header("Authorization", "Bearer $apiKey")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(MistralChatResponse::class.java)

        // Étape 4 : récupération du texte de la réponse
        val reponse = response?.choices?.firstOrNull()?.message?.content
        return reponse ?: "Aucune réponse reçue de Mistral."
    }
}
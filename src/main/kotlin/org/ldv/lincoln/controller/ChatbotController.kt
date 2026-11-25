package org.ldv.lincoln.controller

import org.ldv.lincoln.model.dao.ConversationDAO
import org.ldv.lincoln.model.dao.MessageDAO
import org.ldv.lincoln.model.dao.ModeleIADAO
import org.ldv.lincoln.model.entity.Conversation
import org.ldv.lincoln.model.entity.Message
import org.ldv.lincoln.model.entity.TypeMessage
import org.ldv.lincoln.service.IAServiceFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime


@Controller
class ChatBotController {

    @GetMapping("/connexion")
    fun connexion(): String = "auth/connexion"
}
@Controller
class ChatController(
    private val iaServiceFactory: IAServiceFactory,
    private val conversationDAO: ConversationDAO,
    private val messageDAO: MessageDAO,
    private val modeleIADAO: ModeleIADAO
) {

    // 1. Page d'accueil : liste des modèles et des conversations
    @GetMapping("/")
    fun accueil(model: Model): String {
        model.addAttribute("conversation", null)
        model.addAttribute("modeles", modeleIADAO.findAll())
        model.addAttribute("messages", emptyList<Message>())
        model.addAttribute(
            "conversations",
            conversationDAO.findAllByOrderByDateDerniereModificationDesc()
        )
        return "utilisateur/chat"
    }

    // 2. Création d'une nouvelle conversation
    @PostMapping("/conversations")
    fun creerConversation(
        @RequestParam prompt: String,
        @RequestParam idModele: Long
    ): String {
        val modele = modeleIADAO.findById(idModele)
            .orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Modèle IA introuvable")
            }

        val conversation = conversationDAO.save(
            Conversation(
                titre = prompt.take(50),
                dateCreation = LocalDateTime.now(),
                modeleIA = modele
            )
        )

        messageDAO.save(
            Message(
                contenu = prompt,
                dateCreation = LocalDateTime.now(),
                typeMessage = TypeMessage.UTILISATEUR,
                conversation = conversation
            )
        )

        try {
            val iaService = iaServiceFactory.getService(modele)
            val reponse = iaService.envoyerPrompt(prompt, modele)
            messageDAO.save(
                Message(
                    contenu = reponse,
                    dateCreation = LocalDateTime.now(),
                    typeMessage = TypeMessage.ASSISTANT,
                    conversation = conversation
                )
            )
        } catch (e: Exception) {
            println("Erreur lors de l'appel au modèle ${modele.nom} : ${e.message}")
        }

        conversation.dateDerniereModification = LocalDateTime.now()
        conversationDAO.save(conversation)

        return "redirect:/conversations/${conversation.id}"
    }

    // 3. Affichage d'une conversation existante
    @GetMapping("/conversations/{id}")
    fun afficherConversation(@PathVariable id: Long, model: Model): String {
        val conversation = conversationDAO.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        model.addAttribute(
            "conversations",
            conversationDAO.findAllByOrderByDateDerniereModificationDesc()
        )
        model.addAttribute("conversation", conversation)
        model.addAttribute("messages", conversation.messages.sortedBy { it.dateCreation })
        return "utilisateur/chat"
    }

    // 4. Nouveau message dans une conversation existante
    @PostMapping("/conversations/{id}/messages")
    fun envoyerMessage(
        @PathVariable id: Long,
        @RequestParam prompt: String
    ): String {
        val conversation = conversationDAO.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        messageDAO.save(
            Message(
                contenu = prompt,
                dateCreation = LocalDateTime.now(),
                typeMessage = TypeMessage.UTILISATEUR,
                conversation = conversation
            )
        )

        try {
            val modele = conversation.modeleIA
            val iaService = iaServiceFactory.getService(modele)
            val reponse = iaService.envoyerPrompt(prompt, modele)
            messageDAO.save(
                Message(
                    contenu = reponse,
                    dateCreation = LocalDateTime.now(),
                    typeMessage = TypeMessage.ASSISTANT,
                    conversation = conversation
                )
            )
        } catch (e: Exception) {
            println("Erreur lors de l'appel à ${conversation.modeleIA.nom} : ${e.message}")
        }

        conversation.dateDerniereModification = LocalDateTime.now()
        conversationDAO.save(conversation)

        return "redirect:/conversations/$id"
    }
}
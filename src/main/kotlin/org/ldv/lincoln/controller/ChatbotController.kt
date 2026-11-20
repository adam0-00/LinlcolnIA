package org.ldv.lincoln.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatBotController {

    @GetMapping("/")
    fun accueil(): String = "utilistateur/accueil"

    @GetMapping("/connexion")
    fun connexion(): String = "auth/connexion"
}
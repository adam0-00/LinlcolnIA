package org.ldv.lincoln.service

import org.ldv.lincoln.model.entity.ModeleIA

interface IAService {

    // Identifie le fournisseur pris en charge
    fun getFournisseur(): String

    // Envoie un prompt au modèle choisi et renvoie la réponse
    fun envoyerPrompt(prompt: String, modele: ModeleIA): String
}
package org.ldv.lincoln.model.dao

import org.ldv.lincoln.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UtilisateurDAO : JpaRepository<Utilisateur, Long> {

    @Query("select u from Utilisateur u where upper(u.email) = upper(?1)")
    fun findByEmailIgnoreCase(email: String): Utilisateur?

    @Query("select (count(u) > 0) from Utilisateur u where upper(u.email) = upper(?1)")
    fun existsByEmailIgnoreCase(email: String): Boolean
}
package org.ldv.lincoln.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class `Role`(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    // Nom affiché dans les templates
    var nom: String,

    // Nom utilisé par Spring Security (ROLE_USER, ROLE_ADMIN)
    var nomLogique: String
)
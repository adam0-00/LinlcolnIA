package org.ldv.lincoln.model.entity


import jakarta.persistence.*

@Entity
class FournisseurIA(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    var nom: String,
    var cheminLogo: String,

    @Column(columnDefinition = "TEXT")
    var description: String
)
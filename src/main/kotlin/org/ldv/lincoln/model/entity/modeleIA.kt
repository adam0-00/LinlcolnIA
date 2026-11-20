package org.ldv.lincoln.model.entity

import jakarta.persistence.*

@Entity
class ModeleIA(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    var nom: String,
    var urlAPI: String,
    var cheminLogo: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @ManyToOne
    @JoinColumn(name = "fournisseur_ia_id", nullable = false)
    var fournisseurIA: FournisseurIA
)
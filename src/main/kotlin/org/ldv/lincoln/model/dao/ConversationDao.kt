package org.ldv.lincoln.model.dao

import org.ldv.lincoln.model.entity.Conversation
import org.springframework.data.jpa.repository.JpaRepository

interface Conversasion : JpaRepository<Conversation, Long>
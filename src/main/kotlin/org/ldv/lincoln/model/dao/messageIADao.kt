package org.ldv.lincoln.model.dao


import org.ldv.lincoln.model.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageIADAO : JpaRepository< Message, Long>
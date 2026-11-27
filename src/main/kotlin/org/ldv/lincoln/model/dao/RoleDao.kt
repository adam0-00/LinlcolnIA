package org.ldv.lincoln.model.dao

import org.ldv.lincoln.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleDAO : JpaRepository<Role, Long>
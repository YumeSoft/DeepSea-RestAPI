package com.example.deepsea.repository

import com.example.deepsea.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    override fun findById(id: Long): Optional<User>

    fun findByEmail(email: String): Optional<User>

    fun existsByUsername(username: String): Boolean

    override fun findAll(): List<User>
}

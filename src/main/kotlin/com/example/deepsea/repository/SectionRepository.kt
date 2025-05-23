package com.example.deepsea.repository

import com.example.deepsea.entity.Section
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : JpaRepository<Section, Long> {
    fun findAllByOrderByOrderIndex(): List<Section>

    @Query("SELECT s FROM Section s WHERE s.level = :level ORDER BY s.orderIndex")
    fun findByLevel(level: String): List<Section>
}
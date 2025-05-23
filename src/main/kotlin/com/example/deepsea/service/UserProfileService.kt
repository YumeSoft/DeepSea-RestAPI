package com.example.deepsea.service

import com.example.deepsea.enums.SurveyOption
import com.example.deepsea.dto.UserProfileDto
import com.example.deepsea.exception.UserNotFoundException
import com.example.deepsea.exception.UserProfileNotFoundException
import com.example.deepsea.enums.DailyGoalOption
import com.example.deepsea.entity.UserProfile
import com.example.deepsea.repository.UserProfileRepository
import com.example.deepsea.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UserProfileService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) {
    fun addXp(userId: Long, amount: Int) {
        val profile = userProfileRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("User profile not found")

        profile.totalXp += amount
        userProfileRepository.save(profile)
    }

    fun getUserProfile(userId: Long): UserProfile {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found")
        return user.profile ?: throw UserProfileNotFoundException("Profile not found")
    }

    fun getUserProfileData(userId: Long): UserProfileDto {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found")
        val profile = user.profile ?: throw UserProfileNotFoundException("Profile not found")

        return UserProfileDto(
            name = profile.name,
            username = user.username,
            joinDate = profile.joinDate,
            courses = profile.courses,
            followers = profile.followers,
            avatarUrl = user.avatarUrl,
            following = profile.following,
            dayStreak = profile.dayStreak,
            totalXp = profile.totalXp,
            currentLeague = profile.currentLeague,
            topFinishes = profile.topFinishes,
            friends = profile.friends,
            dailyGoalOption = profile.dailyGoal
        )
    }

    fun saveUserProfile(profile: UserProfile): UserProfile {
        return userProfileRepository.save(profile)
    }

    fun updateSurveySelections(profile: UserProfile, surveys: Set<SurveyOption>): UserProfile {
        val updatedProfile = profile.copy(selectedSurveys = surveys)
        return userProfileRepository.save(updatedProfile)
    }

    fun updateDailyGoal(userId: Long, dailyGoal: DailyGoalOption) {
        val userProfile = userProfileRepository.findById(userId).orElseThrow {
            throw UserNotFoundException("User not found")
        }

        userProfile.dailyGoal = dailyGoal
        userProfileRepository.save(userProfile)
    }

    fun updateProgress(userId: Long, dayStreak: Int, lastLogin: LocalDate) {
        val user = userRepository.findById(userId).orElseThrow { Exception("User not found") }
        val profile = user.profile ?: throw Exception("User profile not found")

        profile.dayStreak = dayStreak
        user.lastLogin = lastLogin
        userRepository.save(user)
    }

    fun updateDayStreak(userId: Long, newDayStreak: Int) {
        val today = LocalDate.now()
        updateProgress(userId, newDayStreak, today)
    }
}
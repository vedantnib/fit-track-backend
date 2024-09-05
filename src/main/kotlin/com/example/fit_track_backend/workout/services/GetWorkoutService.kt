package com.example.fit_track_backend.workout.services

import com.example.fit_track_backend.firestore.FirestoreService
import com.example.fit_track_backend.workout.models.Exercise
import com.example.fit_track_backend.workout.models.Workout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetWorkoutService {
    @Autowired
    private lateinit var firestoreService: FirestoreService

    fun getAllUserWorkouts(userId: String, status: String?= null): List<Workout> {
        return firestoreService.getAllWorkoutsForUser(userId = userId, status = status)
    }

    fun getWorkout(workoutId: String): Workout {
        return firestoreService.getWorkout(workoutId = workoutId)
    }

    fun getAllExercises(userId: String, workoutId: String): List<Exercise> {
        return firestoreService.getAllExercises(userId = userId, workoutId = workoutId)
    }

    fun getExercise(userId: String, workoutId: String, exerciseId: String): Exercise {
        return firestoreService.getExercise(userId = userId, workoutId = workoutId, exerciseId = exerciseId)
    }

    fun getLatestExercise(userId: String, workoutId: String): Exercise {
        return firestoreService.getLatestExercise(userId = userId, workoutId = workoutId)
    }

    fun getLatestCompletedWorkout(userId: String): Workout? {
        return firestoreService.getLatestCompletedWorkout(userId = userId)
    }

    fun getAllExercisesOfWorkoutForUser(userId: String, workoutId: String): List<Exercise> {
        return firestoreService.getAllExercisesOfWorkoutForUser(
            userId = userId,
            workoutId= workoutId
        )
    }

    fun getLastNWorkoutsForUser(userId: String): List<Workout> {
        return firestoreService.getLastNWorkoutsForUser(
            userId = userId,
            n = 100
        )
    }

}
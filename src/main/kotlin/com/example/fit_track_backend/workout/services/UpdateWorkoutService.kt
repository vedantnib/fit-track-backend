package com.example.fit_track_backend.workout.services

import com.example.fit_track_backend.firestore.FirestoreService
import com.example.fit_track_backend.workout.models.Exercise
import com.example.fit_track_backend.workout.models.Workout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UpdateWorkoutService {
    @Autowired
    private lateinit var firestoreService: FirestoreService

    fun updateWorkoutStatusAndEnd(workoutId: String): Workout {
        return firestoreService.updateWorkoutStatusAndEnd(workoutId = workoutId)
    }

    fun updateExercise(
        userId: String,
        workoutId: String,
        exerciseId: String,
        updates: Exercise
    ): Exercise {
        return firestoreService.patchExercise(
            userId = userId,
            workoutId = workoutId,
            exerciseId = exerciseId,
            updates = updates)
    }
}
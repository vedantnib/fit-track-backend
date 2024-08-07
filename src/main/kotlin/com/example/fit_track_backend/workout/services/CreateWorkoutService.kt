package com.example.fit_track_backend.workout.services

import com.example.fit_track_backend.firestore.FirestoreService
import com.example.fit_track_backend.workout.models.Exercise
import com.example.fit_track_backend.workout.models.Workout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreateWorkoutService {
    @Autowired
    private lateinit var firestoreService: FirestoreService

    fun createWorkout(workoutRequest: Workout): Workout {
        //validation
        return firestoreService.addWorkoutSessionToFirestore(workout = workoutRequest)
    }

    fun createExercise(workoutId: String, exerciseRequest: Exercise): Exercise {
        //validation
        return firestoreService.addExerciseToWorkout(workoutId= workoutId, exerciseRequest = exerciseRequest)
    }
}
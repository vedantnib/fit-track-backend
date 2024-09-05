package com.example.fit_track_backend.workout


import com.example.fit_track_backend.workout.models.Exercise
import com.example.fit_track_backend.workout.models.Workout
import com.example.fit_track_backend.workout.models.WorkoutStatus
import com.example.fit_track_backend.workout.services.CreateWorkoutService
import com.example.fit_track_backend.workout.services.GetWorkoutService
import com.example.fit_track_backend.workout.services.UpdateWorkoutService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController()
@CrossOrigin(origins = ["http://localhost:5143"])
class WorkoutController {
    @Autowired
    private lateinit var createWorkoutService: CreateWorkoutService
    @Autowired
    private lateinit var getWorkoutService: GetWorkoutService
    @Autowired
    private lateinit var updateWorkoutService: UpdateWorkoutService
    @GetMapping("/api/v1/ping")
    fun getPing(): String {
        //firestoreService.getDataFromFirestore()
        return "Status: UP"
    }

    @PostMapping("/api/v1/workouts")
    fun createWorkout(
        @RequestBody workoutRequest: Workout
    ): Workout {
        return createWorkoutService.createWorkout(workoutRequest)
    }

    @PostMapping("/api/v1/workouts/{workoutId}/exercise")
    fun createExercise(
        @PathVariable workoutId: String,
        @RequestBody exerciseRequest: Exercise
    ): Exercise {
        return createWorkoutService.createExercise(workoutId, exerciseRequest)
    }



    @GetMapping("/api/v1/workouts/{userId}")
    fun getAllWorkouts(
        @PathVariable userId: String,
        @RequestParam(required = false) status: String?= null
    ): List<Workout> {
        return getWorkoutService.getAllUserWorkouts(userId = userId, status = status)
    }

    @GetMapping("/api/v1/workouts/{userId}/workout/{workoutId}")
    fun getWorkout(
        @PathVariable workoutId: String
    ): Workout {
        return getWorkoutService.getWorkout(workoutId = workoutId)
    }

//    @GetMapping("/api/v1/workouts/{userId}/workout/{workoutId}/exercises")
//    fun getAllExercise(
//        @PathVariable userId: String,
//        @PathVariable workoutId: String,
//    ): List<Exercise> {
//        return getWorkoutService.getAllExercises(userId = userId, workoutId = workoutId)
//    }

    @GetMapping("/api/v1/workouts/{userId}/workout/{workoutId}/exercises/{exerciseId}")
    fun getExercise(
        @PathVariable userId: String,
        @PathVariable workoutId: String,
        @PathVariable exerciseId: String,
    ): Exercise {
        return getWorkoutService.getExercise(userId = userId, workoutId = workoutId, exerciseId=exerciseId)
    }

    @GetMapping("/api/v1/workouts/{userId}/workout/{workoutId}/exercises/latest")
    fun getLatestExercise(
        @PathVariable userId: String,
        @PathVariable workoutId: String
    ): Exercise {
        return getWorkoutService.getLatestExercise(userId = userId, workoutId = workoutId)
    }

    @PatchMapping("/api/v1/workouts/{userId}/workout/{workoutId}")
    fun updateWorkoutStatusAndEnd(
        @PathVariable userId: String,
        @PathVariable workoutId: String,
    ): Workout {
        return updateWorkoutService.updateWorkoutStatusAndEnd(workoutId= workoutId)
    }

    @GetMapping("/api/v1/workouts/{userId}/workout/latest")
    fun getLatestCompletedWorkout(@PathVariable userId: String): Workout? {
        return getWorkoutService.getLatestCompletedWorkout(userId = userId)
    }

    @PatchMapping("/api/v1/workouts/{userId}/workout/{workoutId}/exercises/{exerciseId}")
    fun updateExercise(
        @PathVariable userId: String,
        @PathVariable workoutId: String,
        @PathVariable exerciseId: String,
        @RequestBody updates: Exercise
    ): Exercise? {
        return updateWorkoutService.updateExercise(
            userId = userId,
            workoutId = workoutId,
            exerciseId = exerciseId,
            updates = updates
        )
    }

    @GetMapping("/api/v1/workouts/{userId}/workout/{workoutId}/exercises")
    fun getAllExercises(
        @PathVariable userId: String,
        @PathVariable workoutId: String
    ): List<Exercise>? {
        return getWorkoutService.getAllExercisesOfWorkoutForUser(userId = userId, workoutId = workoutId)
    }

    @GetMapping("/api/v1/workouts/{userId}/latest")
    fun getLastNWorkoutsForUser(@PathVariable userId: String): List<Workout>? {
        return getWorkoutService.getLastNWorkoutsForUser(userId = userId)
    }
}
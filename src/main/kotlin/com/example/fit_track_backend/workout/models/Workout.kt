package com.example.fit_track_backend.workout.models
import java.util.*

data class Workout(
    var dateTime: Date? = null,
    var end: Date?= null,
    var start: Date?= null,
    var userId: String?= null,
    var workoutId: String = UUID.randomUUID().toString(),
    var workoutType: WorkoutType? = null,
    var status: WorkoutStatus ?= WorkoutStatus.IN_PROGRESS
)

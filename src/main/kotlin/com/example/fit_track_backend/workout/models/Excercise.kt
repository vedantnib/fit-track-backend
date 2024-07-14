package com.example.fit_track_backend.workout.models

import java.util.*

data class Exercise(
    var exerciseId: String = UUID.randomUUID().toString(),
    var reps: Int?= 0,
    var weight: Double?= 0.0,
    var srNo: Int ?= 0,
    var exerciseType: ExerciseType ?= null,
    var createdAt: Date
)

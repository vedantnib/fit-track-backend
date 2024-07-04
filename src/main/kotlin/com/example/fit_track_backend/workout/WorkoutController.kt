package com.example.fit_track_backend.workout

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
class WorkoutController {
    @GetMapping("/api/v1/ping")
    fun getPing(): String {
        return "Status: UP"
    }

    @PostMapping("/api/v1/workouts")
    fun createWorkout(
        @PathVariable userId: String
    ): String {
        return "Hello world"
    }
}
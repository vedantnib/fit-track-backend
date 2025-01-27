package com.example.fit_track_backend.user

import com.example.fit_track_backend.user.models.GetUserRequest
import com.example.fit_track_backend.user.models.GetUserResponse
import com.example.fit_track_backend.user.services.UserService
import com.example.fit_track_backend.workout.models.Workout
import com.example.fit_track_backend.workout.services.CreateWorkoutService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController()
@CrossOrigin(origins = ["http://localhost:5143"])
class UserController {
    @Autowired
    private lateinit var userService: UserService


    @PostMapping("/api/v1/login")
    fun createWorkout(
        @RequestBody getUserRequest: GetUserRequest
    ): GetUserResponse {
        return userService.getUserLogin(getUserRequest = getUserRequest)
    }
}
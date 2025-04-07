package com.example.fit_track_backend.user

import com.example.fit_track_backend.user.models.CreateUserRequest
import com.example.fit_track_backend.user.models.GetUserRequest
import com.example.fit_track_backend.user.models.GetUserResponse
import com.example.fit_track_backend.user.models.LoginRequest
import com.example.fit_track_backend.user.services.UserService
import com.example.fit_track_backend.workout.models.Workout
import com.example.fit_track_backend.workout.services.CreateWorkoutService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController()
@CrossOrigin(origins = ["http://localhost:5143"])
class UserController {
    @Autowired
    private lateinit var userService: UserService


    @PostMapping("/api/v1/user/login")
    fun userLogin(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {
        return userService.loginUser(loginRequest = loginRequest)
    }

    @PostMapping("/api/v1/user/register")
    fun registerUser(
        @RequestBody createUserRequest: CreateUserRequest
    ): ResponseEntity<GetUserResponse>? {
        return userService.registerUser(createUserRequest = createUserRequest)
    }
}
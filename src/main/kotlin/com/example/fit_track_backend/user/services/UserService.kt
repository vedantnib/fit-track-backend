package com.example.fit_track_backend.user.services

import com.example.fit_track_backend.firestore.FirestoreService
import com.example.fit_track_backend.user.models.CreateUserRequest
import com.example.fit_track_backend.user.models.GetUserRequest
import com.example.fit_track_backend.user.models.GetUserResponse
import com.example.fit_track_backend.user.models.LoginRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var firestoreService: FirestoreService
    fun getUserLogin(getUserRequest: GetUserRequest): GetUserResponse {
        return firestoreService.getUserToken(getUserRequest = getUserRequest)
    }

    fun registerUser(createUserRequest: CreateUserRequest): ResponseEntity<GetUserResponse>? {
        return firestoreService.registerUser(createUserRequest = createUserRequest)
    }

    fun loginUser(loginRequest: LoginRequest):ResponseEntity<Any> {
        return firestoreService.loginUser(loginRequest = loginRequest)
    }
}
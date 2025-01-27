package com.example.fit_track_backend.user.services

import com.example.fit_track_backend.firestore.FirestoreService
import com.example.fit_track_backend.user.models.GetUserRequest
import com.example.fit_track_backend.user.models.GetUserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var firestoreService: FirestoreService
    fun getUserLogin(getUserRequest: GetUserRequest): GetUserResponse {
        return firestoreService.getUserToken(getUserRequest = getUserRequest)
    }
}
package com.example.fit_track_backend.user.models

import java.util.*

data class CreateUserRequest(
    val userName: String,
    val password: String,
    val email: String,
    val lastSeenAt: Date = Date()
)

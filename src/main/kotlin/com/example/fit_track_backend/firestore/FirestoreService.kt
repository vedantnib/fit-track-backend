package com.example.fit_track_backend.firestore

import com.example.fit_track_backend.user.models.GetUserRequest
import com.example.fit_track_backend.user.models.GetUserResponse
import com.example.fit_track_backend.workout.models.*
import com.google.api.core.ApiFuture
import com.google.cloud.Timestamp
import com.google.cloud.firestore.*
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Service
import java.sql.Date
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ExecutionException


@Service
open class FirestoreService {
    val db: Firestore = FirestoreClient.getFirestore()
    val workoutCollection: CollectionReference = db.collection("workouts")
    val userCollection: CollectionReference = db.collection("users")

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getDataFromFirestore(): Any? {
        val db: Firestore = FirestoreClient.getFirestore()
        val document = workoutCollection.get()
            .get() // Replace "yourDocumentId" with the actual document ID
        var docs = document.documents
        return docs// Return the data from Firestore document
    }

    fun addWorkoutSessionToFirestore(workout: Workout): Workout {
        val collection: CollectionReference = db.collection("workouts")
        collection.document(workout.workoutId).set(workout)
        val result: ApiFuture<WriteResult> = collection.document(workout.workoutId).set(workout)
        result.get()
        return workout
    }

    fun addExerciseToWorkout(workoutId: String, exerciseRequest: Exercise): Exercise {
        val exercisesCollection = workoutCollection.document(workoutId).collection("exercises")
        val result: ApiFuture<WriteResult> = exercisesCollection.document(exerciseRequest.exerciseId).set(exerciseRequest)
        result.get()
        return exerciseRequest
    }
@Throws(ExecutionException::class, InterruptedException::class)
fun getAllWorkoutsForUser(userId: String, status: String? = null): List<Workout> {
    val workouts = mutableListOf<Workout>()
    val query = workoutCollection.whereEqualTo("userId", userId)

    val querySnapshot = if (status != null) {
        query.whereEqualTo("status", status).get().get()
    } else {
        query.get().get()
    }

    for (document in querySnapshot.documents) {
        val workoutId = document.id
        val workoutType = document.getString("workoutType")?.let { WorkoutType.valueOf(it) }

        val workout = Workout(
            workoutId = workoutId,
            userId = userId,
            workoutType = workoutType,
            status = WorkoutStatus.valueOf(document.get("status") as String),
            start = (document.get("start") as Timestamp).toDate(),
            // end = data["end"].let { (it as Timestamp).toDate() },
            dateTime = (document["dateTime"] as Timestamp).toDate(),
        )
        workouts.add(workout)
    }
    return workouts
}
    fun getWorkout(workoutId: String): Workout {
        val workoutDocument = workoutCollection.document(workoutId).get().get()
        val data = workoutDocument.data ?: throw IllegalArgumentException("Workout not found")

        return Workout(
            userId = data["userId"] as String,
            workoutId = workoutDocument.id,
            workoutType = WorkoutType.valueOf(data["workoutType"] as String),
            status = WorkoutStatus.valueOf(data["status"] as String),
            start = (data["start"] as Timestamp).toDate(),
           // end = data["end"].let { (it as Timestamp).toDate() },
            dateTime = (data["dateTime"] as Timestamp).toDate(),
        )

    }

    fun getAllExercises(userId: String, workoutId: String): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        val exercisesCollection = workoutCollection.document(workoutId).collection("exercises")

        val querySnapshot = exercisesCollection.get().get()

        for (document in querySnapshot.documents) {
            val exercise = Exercise(
                exerciseId = document.id,
                exerciseType = document.getString("exerciseType")?.let { ExerciseType.valueOf(it) },
                reps = (document.getLong("reps") ?: 0).toInt(),
                weight = (document.getDouble("weight") ?: 0.0),
                srNo = (document.getLong("srNo") ?: 0).toInt(),
                createdAt = (document.get("createdAt") as Timestamp).toDate()
            )
            exercises.add(exercise)
        }
        return exercises
    }

    fun getExercise(userId: String, workoutId: String, exerciseId: String): Exercise {
        val exerciseDocument = workoutCollection.document(workoutId).collection("exercises").document(exerciseId).get().get()
        val data = exerciseDocument.data ?: throw IllegalArgumentException("Exercise not found")
        val weight = when (val weightData = data["weight"]) {
            is Long -> weightData.toDouble()
            is Double -> weightData
            else -> throw IllegalArgumentException("Invalid weight type")
        }

        return Exercise(
            exerciseId = exerciseDocument.id,
            exerciseType = data["exerciseType"]?.let { ExerciseType.valueOf(it as String) },
            reps = (data["reps"] as Long).toInt(),
            weight = weight,
            srNo = (data["srNo"] as Long).toInt(),
            createdAt = (data["createdAt"] as Timestamp).toDate()
        )
    }

    fun getLatestExercise(userId: String, workoutId: String): Exercise {
        val exercisesCollection = workoutCollection.document(workoutId).collection("exercises")
        val query = exercisesCollection.orderBy("createdAt", Query.Direction.DESCENDING).limit(1)
        val querySnapshot = query.get().get()
        val document = querySnapshot.documents.firstOrNull() ?: throw IllegalArgumentException("No exercises found for the workout")

        val data = document.data ?: throw IllegalArgumentException("Exercise data not found")
        val weight = when (val weightData = data["weight"]) {
            is Long -> weightData.toDouble()
            is Double -> weightData
            else -> throw IllegalArgumentException("Invalid weight type")
        }

        return Exercise(
            exerciseId = document.id,
            exerciseType = data["exerciseType"]?.let { ExerciseType.valueOf(it as String) },
            reps = (data["reps"] as Long).toInt(),
            weight = weight,
            srNo = (data["srNo"] as Long).toInt(),
            createdAt = (data["createdAt"] as Timestamp).toDate()
        )
    }

    fun updateWorkoutStatusAndEnd(workoutId: String): Workout {
        val workoutRef = workoutCollection.document(workoutId)
        val updates = hashMapOf<String, Any>(
            "status" to WorkoutStatus.COMPLETED,
            "end" to Timestamp.now()
        )
        workoutRef.update(updates).get()

        val updatedWorkoutDoc = workoutRef.get().get()
        val data = updatedWorkoutDoc.data ?: throw IllegalArgumentException("Workout not found")

        return Workout(
            userId = data["userId"] as String,
            workoutId = updatedWorkoutDoc.id,
            workoutType = WorkoutType.valueOf(data["workoutType"] as String),
            status = WorkoutStatus.valueOf(data["status"] as String),
            start = (data["start"] as Timestamp).toDate(),
            end = (data["end"] as Timestamp).toDate(),
            dateTime = (data["dateTime"] as Timestamp).toDate()
        )
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getLatestCompletedWorkout(userId: String): Workout? {
        val querySnapshot = workoutCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", WorkoutStatus.COMPLETED.name)
            .orderBy("end", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .get()

        val document = querySnapshot.documents.firstOrNull() ?: return null

        val data = document.data ?: throw IllegalArgumentException("Workout data not found")

        return Workout(
            userId = data["userId"] as String,
            workoutId = document.id,
            workoutType = WorkoutType.valueOf(data["workoutType"] as String),
            status = WorkoutStatus.valueOf(data["status"] as String),
            start = (data["start"] as Timestamp).toDate(),
            end = (data["end"] as Timestamp).toDate(),
            dateTime = (data["dateTime"] as Timestamp).toDate()
        )
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getLastNWorkoutsForUser(userId: String, n: Int): List<Workout> {
        val workouts = mutableListOf<Workout>()
        val query = workoutCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", WorkoutStatus.COMPLETED.name)
            .orderBy("end", Query.Direction.DESCENDING)
            .limit(n)

        val querySnapshot = query.get().get()
        for (document in querySnapshot.documents) {
            val data = document.data ?: continue
            val workout = Workout(
                workoutId = document.id,
                userId = data["userId"] as String,
                workoutType = WorkoutType.valueOf(data["workoutType"] as String),
                status = WorkoutStatus.valueOf(data["status"] as String),
                start = (data["start"] as Timestamp).toDate(),
                end = (data["end"] as Timestamp).toDate(),
                dateTime = (data["dateTime"] as Timestamp).toDate()
            )
            workouts.add(workout)
        }
        return workouts
    }

    fun patchExercise(userId: String, workoutId: String, exerciseId: String, updates: Exercise): Exercise {
        val exerciseRef = workoutCollection.document(workoutId).collection("exercises").document(exerciseId)

        val updateData = mutableMapOf<String, Any>()

        updates.exerciseType?.let { updateData["exerciseType"] = it.name }
        updates.reps?.let { updateData["reps"] = it }
        updates.weight?.let { updateData["weight"] = it }
        updates.srNo?.let { updateData["srNo"] = it }

        exerciseRef.update(updateData).get()

        val updatedExerciseDoc = exerciseRef.get().get()
        val data = updatedExerciseDoc.data ?: throw IllegalArgumentException("Exercise not found")
        val weight = when (val weightData = data["weight"]) {
            is Long -> weightData.toDouble()
            is Double -> weightData
            else -> throw IllegalArgumentException("Invalid weight type")
        }
        return Exercise(
            exerciseId = updatedExerciseDoc.id,
            exerciseType = data["exerciseType"]?.let { ExerciseType.valueOf(it as String) },
            reps = (data["reps"] as Long).toInt(),
            weight = weight,
            srNo = (data["srNo"] as Long).toInt(),
            createdAt = (data["createdAt"] as Timestamp).toDate()
        )
    }


    @Throws(ExecutionException::class, InterruptedException::class)
    fun getLatestWorkoutOfType(userId: String, workoutType: WorkoutType): Workout? {
        val querySnapshot = workoutCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("workoutType", workoutType.name)
            .orderBy("start", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .get()

        val document = querySnapshot.documents.firstOrNull() ?: return null

        val data = document.data ?: throw IllegalArgumentException("Workout data not found")

        return Workout(
            userId = data["userId"] as String,
            workoutId = document.id,
            workoutType = WorkoutType.valueOf(data["workoutType"] as String),
            status = WorkoutStatus.valueOf(data["status"] as String),
            start = (data["start"] as Timestamp).toDate(),
            end = (data["end"] as Timestamp).toDate(),
            dateTime = (data["dateTime"] as Timestamp).toDate()
        )
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getAllExercisesOfWorkoutForUser(userId: String, workoutId: String): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        val exercisesCollection = workoutCollection.document(workoutId).collection("exercises")

        val querySnapshot = exercisesCollection.orderBy("createdAt", Query.Direction.ASCENDING).get().get()

        for (document in querySnapshot.documents) {
            val data = document.data ?: continue
            val weight = when (val weightData = data["weight"]) {
                is Long -> weightData.toDouble()
                is Double -> weightData
                else -> throw IllegalArgumentException("Invalid weight type")
            }
            val exercise = Exercise(
                exerciseId = document.id,
                exerciseType = data["exerciseType"]?.let { ExerciseType.valueOf(it as String) },
                reps = (data["reps"] as Long).toInt(),
                weight = weight,
                srNo = (data["srNo"] as Long).toInt(),
                createdAt = (data["createdAt"] as Timestamp).toDate()
            )
            exercises.add(exercise)
        }
        return exercises
    }

    fun getUserToken(getUserRequest: GetUserRequest): GetUserResponse {
        // Fetch the user from Firestore using userName
        val querySnapshot: QuerySnapshot = userCollection
            .whereEqualTo("userName", getUserRequest.userName)
            .get()
            .get()

        // If user does not exist, throw an error
        if (querySnapshot.documents.isEmpty()) {
            throw IllegalArgumentException("User not found")
        }

        val userDocument = querySnapshot.documents[0]
        val storedPassword = userDocument.getString("password")

        // Validate the provided password with the stored password
        if (storedPassword != getUserRequest.password) {
            throw IllegalArgumentException("Invalid password")
        }

        // Generate a token (for demo purposes, using a simple random UUID as a token)
        val token = UUID.randomUUID().toString()

        // Return the token in the response
        return GetUserResponse(token = token, userId = getUserRequest.userName)
    }

}
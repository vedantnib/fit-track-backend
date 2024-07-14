package com.example.fit_track_backend.config
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.IOException
import javax.annotation.PostConstruct

@Service
public class FirebaseInitializer {
	@PostConstruct
    public fun initializeFirebase() {
		try {
			val serviceAccount = FileInputStream("src/main/resources/beta-dbved-firebase-adminsdk-o0sn7-c41268a78e.json")
			val options: FirebaseOptions = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build()
			FirebaseApp.initializeApp(options)
		}
		catch (exception: Exception) {
			print("Error "+ exception.message)
		}

    }
}
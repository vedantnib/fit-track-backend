package com.example.fit_track_backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import javax.annotation.PostConstruct

@Service
class FirebaseInitializer {
	@PostConstruct
	fun initializeFirebase() {
		try {
			val privateKey = System.getenv("GOOGLE_PRIVATE_KEY")?.replace("\\n", "\n")
				?: throw IllegalStateException("Environment variable GOOGLE_PRIVATE_KEY is not set")

			val options = FirebaseOptions.builder()
				.setCredentials(
					GoogleCredentials.fromStream(
						ByteArrayInputStream(
							("{\"type\": \"service_account\"," +
									"\"project_id\": \"" + System.getenv("GOOGLE_PROJECT_ID") + "\"," +
									"\"private_key_id\": \"" + System.getenv("GOOGLE_PRIVATE_KEY_ID") + "\"," +
									"\"private_key\": \"" + privateKey + "\"," +
									"\"client_email\": \"" + System.getenv("GOOGLE_CLIENT_EMAIL") + "\"," +
									"\"client_id\": \"" + System.getenv("GOOGLE_CLIENT_ID") + "\"," +
									"\"auth_uri\": \"" + System.getenv("GOOGLE_AUTH_URI") + "\"," +
									"\"token_uri\": \"" + System.getenv("GOOGLE_TOKEN_URI") + "\"," +
									"\"auth_provider_x509_cert_url\": \"" + System.getenv("GOOGLE_AUTH_PROVIDER_X509_CERT_URL") + "\"," +
									"\"client_x509_cert_url\": \"" + System.getenv("GOOGLE_CLIENT_X509_CERT_URL") + "\"," +
									"\"universe_domain\": \"" + System.getenv("GOOGLE_UNIVERSE_DOMAIN") + "\"}")
								.toByteArray()
						)
					)
				)
				.build()

			FirebaseApp.initializeApp(options)
			println("Firebase initialized successfully")
		} catch (exception: Exception) {
			println("Error initializing Firebase: ${exception.message}")
			exception.printStackTrace()
		}
	}
}
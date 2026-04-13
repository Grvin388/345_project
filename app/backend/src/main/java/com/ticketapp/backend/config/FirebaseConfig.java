package com.ticketapp.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    protected boolean shouldInitializeFirebase() {
        return FirebaseApp.getApps().isEmpty();
    }

    protected InputStream getServiceAccountStream() {
        return getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");
    }

    protected GoogleCredentials loadCredentials(InputStream serviceAccount) throws IOException {
        return GoogleCredentials.fromStream(serviceAccount);
    }

    protected void initializeFirebaseApp(FirebaseOptions options) {
        FirebaseApp.initializeApp(options);
    }

    @PostConstruct
    public void init() {
        try {
            if (shouldInitializeFirebase()) {
                InputStream serviceAccount = getServiceAccountStream();

                if (serviceAccount == null) {
                    throw new RuntimeException("firebase-service-account.json not found in resources folder");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(loadCredentials(serviceAccount))
                        .build();

                initializeFirebaseApp(options);
                System.out.println("Firebase initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}
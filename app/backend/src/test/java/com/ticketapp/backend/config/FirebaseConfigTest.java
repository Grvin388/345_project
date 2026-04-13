package com.ticketapp.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class FirebaseConfigTest {

    @Test
    void init_doesNothingWhenFirebaseIsAlreadyInitialized() {
        try (MockedStatic<FirebaseApp> firebaseAppStatic = mockStatic(FirebaseApp.class)) {
            firebaseAppStatic.when(FirebaseApp::getApps).thenReturn(List.of(mock(FirebaseApp.class)));

            assertDoesNotThrow(() -> new FirebaseConfig().init());
        }
    }

    @Test
    void init_throwsWhenCredentialsLoadingFails() {
        try (MockedStatic<FirebaseApp> firebaseAppStatic = mockStatic(FirebaseApp.class);
                MockedStatic<GoogleCredentials> googleCredentialsStatic = mockStatic(GoogleCredentials.class)) {

            firebaseAppStatic.when(FirebaseApp::getApps).thenReturn(List.of());
            googleCredentialsStatic.when(() -> GoogleCredentials.fromStream(any(InputStream.class)))
                    .thenThrow(new RuntimeException("firebase-service-account.json not found in resources folder"));

            assertThatThrownBy(() -> new FirebaseConfig().init())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Failed to initialize Firebase")
                    .hasCauseInstanceOf(RuntimeException.class)
                    .rootCause()
                    .hasMessage("firebase-service-account.json not found in resources folder");
        }
    }

    @Test
    void init_initializesFirebaseWhenServiceAccountExists() {
        try (MockedStatic<FirebaseApp> firebaseAppStatic = mockStatic(FirebaseApp.class);
                MockedStatic<GoogleCredentials> googleCredentialsStatic = mockStatic(GoogleCredentials.class)) {

            firebaseAppStatic.when(FirebaseApp::getApps).thenReturn(List.of());
            googleCredentialsStatic.when(() -> GoogleCredentials.fromStream(any(InputStream.class)))
                    .thenReturn(mock(GoogleCredentials.class));

            assertDoesNotThrow(() -> new FirebaseConfig().init());
        }
    }
}
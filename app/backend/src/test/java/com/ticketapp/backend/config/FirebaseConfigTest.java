package com.ticketapp.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class FirebaseConfigTest {

    @Test
    void init_doesNothingWhenFirebaseIsAlreadyInitialized() {
        TestFirebaseConfig firebaseConfig = new TestFirebaseConfig();
        firebaseConfig.shouldInitialize = false;

        firebaseConfig.init();

        assertThat(firebaseConfig.initializedOptions).isNull();
        assertThat(firebaseConfig.credentialsLoaded).isFalse();
    }

    @Test
    void init_throwsWhenServiceAccountIsMissing() {
        TestFirebaseConfig firebaseConfig = new TestFirebaseConfig();
        firebaseConfig.shouldInitialize = true;
        firebaseConfig.serviceAccountStream = null;

        assertThatThrownBy(firebaseConfig::init)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to initialize Firebase")
                .hasCauseInstanceOf(RuntimeException.class)
                .rootCause()
                .hasMessage("firebase-service-account.json not found in resources folder");
    }

    @Test
    void init_initializesFirebaseWhenServiceAccountExists() {
        TestFirebaseConfig firebaseConfig = new TestFirebaseConfig();
        firebaseConfig.shouldInitialize = true;
        firebaseConfig.serviceAccountStream = new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8));
        firebaseConfig.credentials = mock(GoogleCredentials.class);

        firebaseConfig.init();

        assertThat(firebaseConfig.credentialsLoaded).isTrue();
        assertThat(firebaseConfig.initializedOptions).isNotNull();
    }

    private static final class TestFirebaseConfig extends FirebaseConfig {
        private boolean shouldInitialize;
        private InputStream serviceAccountStream;
        private GoogleCredentials credentials;
        private boolean credentialsLoaded;
        private FirebaseOptions initializedOptions;

        @Override
        protected boolean shouldInitializeFirebase() {
            return shouldInitialize;
        }

        @Override
        protected InputStream getServiceAccountStream() {
            return serviceAccountStream;
        }

        @Override
        protected GoogleCredentials loadCredentials(InputStream serviceAccount) {
            credentialsLoaded = true;
            return credentials;
        }

        @Override
        protected void initializeFirebaseApp(FirebaseOptions options) {
            initializedOptions = options;
        }
    }
}
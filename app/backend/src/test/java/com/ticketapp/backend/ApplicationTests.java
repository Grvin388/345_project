package com.ticketapp.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ticketapp.backend.config.FirebaseConfig;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {

	@MockitoBean
	private FirebaseConfig firebaseConfig;

	@Test
	void contextLoads() {
	}

}

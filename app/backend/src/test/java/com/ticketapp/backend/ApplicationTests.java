package com.ticketapp.backend;

import com.ticketapp.backend.config.FirebaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ApplicationTests {

	@MockitoBean
	private FirebaseConfig firebaseConfig;

	@Test
	void contextLoads() {
	}

}

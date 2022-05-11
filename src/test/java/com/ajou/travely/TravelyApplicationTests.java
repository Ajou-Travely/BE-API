package com.ajou.travely;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
		"auth.kakaoOauth2ClinetId=test",
		"auth.frontendRedirectUrl=test",
})
class TravelyApplicationTests {

	@Test
	void contextLoads() {
	}

}

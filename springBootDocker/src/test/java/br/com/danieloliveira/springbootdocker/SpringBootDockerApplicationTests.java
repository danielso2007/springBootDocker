package br.com.danieloliveira.springbootdocker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.BDDAssertions.then;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringBootDockerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootDockerApplicationTests {

	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate testRestTemplate;
	private ConfigurableApplicationContext application1;

	@BeforeEach
	void startApps() {
		application1 = startApp(8290);
	}

	@AfterEach
	void closeApps() {
		application1.close();
	}

	@Test
    void shouldRoundRobinOverInstancesWhenCallingServicesViaLoadBalancer() {
        ResponseEntity<String> response1 = testRestTemplate.getForEntity("http://localhost:" + port + "/", String.class);
        then(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response1.getBody()).isEqualTo("Hello World!");
    }

	private ConfigurableApplicationContext startApp(int port) {
        return SpringApplication.run(SpringBootDockerApplication.class, "--server.port=" + port, "--spring.jmx.enabled=false", "--spring.profiles.active=test");
    }

}

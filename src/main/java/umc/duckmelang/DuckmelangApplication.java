package umc.duckmelang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DuckmelangApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuckmelangApplication.class, args);
	}
}

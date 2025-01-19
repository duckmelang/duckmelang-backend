package umc.duckmelang;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import umc.duckmelang.domain.application.dto.SentApplicationDto;
import umc.duckmelang.domain.application.service.ApplicationQueryService;

@SpringBootApplication
@EnableJpaAuditing
public class DuckmelangApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuckmelangApplication.class, args);
	}
}

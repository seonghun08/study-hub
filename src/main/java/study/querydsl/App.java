package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	// spring security 로 할거면 principle 꺼내서 넣어주면 된다!
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}
}

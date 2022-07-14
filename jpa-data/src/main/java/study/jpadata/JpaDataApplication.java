package study.jpadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing    //	Auditing 기능 사용
@SpringBootApplication
public class JpaDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaDataApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorPrivider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
//	return new AuditorAware<String>() {	// 인터페이스에서 메서드가 하나면 람다로 변경가능하다
//		@Override
//		public Optional<String> getCurrentAuditor() {
//			return Optional.of(UUID.randomUUID().toString());
//		}
//	};

}

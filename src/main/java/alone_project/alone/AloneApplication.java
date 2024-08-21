package alone_project.alone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // created_time, updated_time 자동으로 업데이트
@SpringBootApplication
public class AloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(AloneApplication.class, args);
	}

}

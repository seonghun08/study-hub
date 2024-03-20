package hello.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hello.aop")
public class aopApplication {

	public static void main(String[] args) {
		SpringApplication.run(aopApplication.class, args);
	}
}

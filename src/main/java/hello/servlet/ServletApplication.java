package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ServletComponentScan
@SpringBootApplication
public class ServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}

	/*
	 * - "SpringBoot"는 "properties"에서 따로 설정할 수 있도록 도와준다.
	 * - application.properties
	 *   spring.mvc.view.prefix=/WEB-INF/views/
	 *   spring.mvc.view.suffix=.jsp
	 */
//	@Bean
//	ViewResolver internalResourceViewResolver() {
//		return new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
//	}
}

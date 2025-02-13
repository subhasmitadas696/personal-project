package com.csmtech.sjta;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class SjtaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SjtaApplication.class, args);
	}

	@Configuration
	public class CorsConfig {

		@Value("${allowed.origins}") 
		private String allowedOrigins;

		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**").allowedOrigins(allowedOrigins.split(","));
				}
			};
		} 
	}
}

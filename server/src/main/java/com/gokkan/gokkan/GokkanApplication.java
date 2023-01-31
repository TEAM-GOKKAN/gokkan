package com.gokkan.gokkan;

import com.gokkan.gokkan.global.security.config.properties.AppProperties;
import com.gokkan.gokkan.global.security.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
	CorsProperties.class,
	AppProperties.class
})
public class GokkanApplication {

	public static void main(String[] args) {
		SpringApplication.run(GokkanApplication.class, args);
	}

}

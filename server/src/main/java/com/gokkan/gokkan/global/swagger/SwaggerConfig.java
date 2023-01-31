package com.gokkan.gokkan.global.swagger;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.converters.WebFluxSupportConverter;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(@Value("${springdoc.version}") String version) {

		Info info = new Info()
			.title("곳간 API 문서") // 타이틀
			.version(version) // 문서 버전
			.description("곳간 API 문서입니다.");

		// Security 스키마 설정
		SecurityScheme bearerAuth = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name(HttpHeaders.AUTHORIZATION);

		// Security 요청 설정
		SecurityRequirement addSecurityItem = new SecurityRequirement();
		addSecurityItem.addList("JWT");

		ModelConverters.getInstance()
			.addConverter(new WebFluxSupportConverter(new ObjectMapperProvider(
				new SpringDocConfigProperties())));
		return new OpenAPI()
			// Security 인증 컴포넌트 설정
			.components(new Components().addSecuritySchemes("JWT", bearerAuth))
			// API 마다 Security 인증 컴포넌트 설정
			.addSecurityItem(addSecurityItem)
			.info(info);
	}
}

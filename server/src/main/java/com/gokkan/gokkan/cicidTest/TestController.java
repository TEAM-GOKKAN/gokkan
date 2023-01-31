package com.gokkan.gokkan.cicidTest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "서버 오픈 테스트 api", description = "서버 오픈 테스트 api")
@RestController
public class TestController {

	@GetMapping("")
	public static String hello() {
		return "web hook test gokkan";
	}
}

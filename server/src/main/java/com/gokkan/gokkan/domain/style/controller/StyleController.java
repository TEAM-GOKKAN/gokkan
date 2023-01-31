package com.gokkan.gokkan.domain.style.controller;

import static com.gokkan.gokkan.domain.style.dto.StyleDto.CreateRequest;
import static com.gokkan.gokkan.domain.style.dto.StyleDto.Response;
import static com.gokkan.gokkan.domain.style.dto.StyleDto.UpdateRequest;

import com.gokkan.gokkan.domain.style.service.StyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "스타일 컨트롤러", description = "스타일 저장, 읽기, 수정, 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/styles")
public class StyleController {

	private final StyleService styleService;

	@Operation(summary = "스타일 생성", description = "스타일 생성")
	@ApiResponse(responseCode = "201", description = "생성된 스타일 반환", content = @Content(schema = @Schema(implementation = Response.class)))
	@PostMapping("")
	public ResponseEntity<?> create(
		@Parameter(description = "스타일 생성 정보", required = true, content = @Content(schema = @Schema(implementation = CreateRequest.class)))
		@Validated @RequestBody CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(styleService.create(request));
	}

	@Operation(summary = "스타일 읽기", description = "스타일 읽기")
	@ApiResponse(responseCode = "200", description = "id 값에 맞는 스타일 반환", content = @Content(schema = @Schema(implementation = Response.class)))
	@GetMapping("")
	public ResponseEntity<?> read(
		@Parameter(description = "스타일 name", required = true)
		@RequestParam String name) {
		return ResponseEntity.status(HttpStatus.OK).body(styleService.read(name));
	}

	@DeleteMapping("")
	public ResponseEntity<?> delete(
		@Parameter(description = "스타일 name", required = true)
		@RequestParam String name) {
		return ResponseEntity.status(HttpStatus.OK).body(styleService.delete(name));
	}

	@Operation(summary = "스타일 수정", description = "스타일 수정")
	@ApiResponse(responseCode = "200", description = "수정된 스타일 반환", content = @Content(schema = @Schema(implementation = Response.class)))
	@PutMapping("")
	public ResponseEntity<?> update(
		@Parameter(description = "스타일 수정 정보", required = true, content = @Content(schema = @Schema(implementation = UpdateRequest.class)))
		@Validated @RequestBody UpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(styleService.update(request));
	}
}

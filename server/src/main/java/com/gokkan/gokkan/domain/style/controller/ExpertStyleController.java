package com.gokkan.gokkan.domain.style.controller;

import com.gokkan.gokkan.domain.style.service.ExpertStyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expert/style")
@RequiredArgsConstructor
@Tag(name = "전문가 스타일 컨트롤러", description = "전문가 스타일 컨트롤러")
public class ExpertStyleController {

	private final ExpertStyleService expertStyleService;

	@PostMapping
	@Operation(summary = "전문가 스타일 생성(스타일 아이디)", description = "전문가 스타일 생성(스타일 아이디)")
	public void createStyleByStyleId(
		@Parameter(description = "전문가 정보 ID")
		@RequestParam Long expertInfoId,
		@Parameter(description = "스타일 ID")
		@RequestParam Long styleId) {
		expertStyleService.createStyleByStyleId(expertInfoId, styleId);
	}

	@PostMapping("/styleName")
	@Operation(summary = "전문가 스타일 생성(스타일 이름)", description = "전문가 스타일 생성(스타일 이름)")
	public void createStyleByStyleName(
		@Parameter(description = "전문가 정보 ID")
		@RequestParam Long expertInfoId,
		@Parameter(description = "스타일 이름")
		@RequestParam String styleName) {
		expertStyleService.createStyleByStyleName(expertInfoId, styleName);
	}

	@DeleteMapping
	@Operation(summary = "전문가 스타일 삭제", description = "전문가 스타일 삭제")
	public void deleteExpertStyle(
		@Parameter(description = "전문가 스타일 ID")
		@RequestParam Long expertStyleId) {
		expertStyleService.deleteExpertStyle(expertStyleId);
	}

	@DeleteMapping("/styleName")
	@Operation(summary = "전문가 스타일 삭제(스타일 이름)", description = "전문가 스타일 삭제(스타일 이름)")
	public void deleteStyleByStyleName(
		@Parameter(description = "전문가 정보 ID")
		@RequestParam Long expertInfoId,
		@Parameter(description = "스타일 ID")
		@RequestParam String styleName) {
		expertStyleService.deleteStyleByStyleName(expertInfoId, styleName);
	}

	@GetMapping
	@Operation(summary = "전문가 스타일 리스트 조회", description = "전문가 스타일 리스트 조회")
	public ResponseEntity<List<String>> getExpertStyles(
		@Parameter(description = "전문가 정보 ID")
		@RequestParam Long expertInfoId) {
		return ResponseEntity.ok(expertStyleService.getExpertStyles(expertInfoId));
	}

}
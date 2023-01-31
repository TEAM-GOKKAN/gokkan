package com.gokkan.gokkan.domain.expertInfo.controller;

import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByMemberId;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByNickName;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.ResponseGetExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.service.ExpertInfoService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expert/info")
@Tag(name = "전문가 정보 컨트롤러", description = "전문가 정보 컨트롤러")
@RequiredArgsConstructor
public class ExpertInfoController {

	private final ExpertInfoService expertInfoService;

	@PostMapping
	@Operation(summary = "전문가 정보 생성 (멤버 아이디)", description = "전문가 정보 생성 (멤버 아이디)")
	public void createExpertInfoByMemberId(
		@Parameter(content = @Content(schema = @Schema(implementation = RequestCreateExpertInfoByMemberId.class)))
		@Validated @RequestBody RequestCreateExpertInfoByMemberId requestCreateExpertInfoByMemberId) {
		expertInfoService.createExpertInfoByMemberId(requestCreateExpertInfoByMemberId);
	}

	@PostMapping("/nick-name")
	@Operation(summary = "전문가 정보 생성 (멤버 닉네임)", description = "전문가 정보 생성 (멤버 닉네임)")
	public void createExpertInfoByName(
		@Parameter(content = @Content(schema = @Schema(implementation = RequestCreateExpertInfoByNickName.class)))
		@Validated @RequestBody RequestCreateExpertInfoByNickName requestCreateExpertInfoByNickName) {
		expertInfoService.createExpertInfoByNickName(requestCreateExpertInfoByNickName);
	}

	@PatchMapping("/my-info")
	@Operation(summary = "내 전문가 정보 수정", description = "내 전문가 정보 수정")
	public void updateExpertInfo(
		@Parameter(hidden = true) @CurrentMember Member member,
		@RequestParam String info) {
		expertInfoService.updateExpertInfo(member, info);
	}

	@GetMapping("/my-info")
	@ApiResponse(description = "현재 회원 정보", content = @Content(schema = @Schema(implementation = ResponseGetExpertInfo.class)))
	@Operation(summary = "내 전문가 정보 조회", description = "내 전문가 정보 조회")
	public ResponseEntity<ResponseGetExpertInfo> getExpertInfo(
		@Parameter(hidden = true) @CurrentMember Member member) {
		return ResponseEntity.ok(expertInfoService.getExpertInfo(member));
	}

	@GetMapping("/is-expert")
	@Operation(summary = "전문가 여부 조회", description = "전문가 여부 조회")
	public ResponseEntity<Boolean> isExpert(
		@Parameter(hidden = true) @CurrentMember Member member) {
		return ResponseEntity.ok(expertInfoService.isExpert(member));
	}

}
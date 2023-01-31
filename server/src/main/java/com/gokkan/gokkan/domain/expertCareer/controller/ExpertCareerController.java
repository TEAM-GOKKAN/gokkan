package com.gokkan.gokkan.domain.expertCareer.controller;

import com.gokkan.gokkan.domain.expertCareer.domain.dto.ExpertCareerDto.RequestCreateExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.domain.dto.ExpertCareerDto.RequestUpdateExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.domain.dto.ExpertCareerDto.ResponseGetExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.service.ExpertCareerService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expert/career")
@Tag(name = "전문가 커리어 컨트롤러", description = "전문가 커리어 컨트롤러")
public class ExpertCareerController {

	private final ExpertCareerService expertCareerService;

	@PostMapping("/my-carrer")
	@Operation(summary = "내 전문가 커리어 생성", description = "전문가 커리어 생성")
	public void createExpertCareer(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(content = @Content(schema = @Schema(implementation = RequestCreateExpertCareer.class)))
		@Validated @RequestBody List<RequestCreateExpertCareer> requestCreateExpertCareer) {
		expertCareerService.createExpertCareer(member, requestCreateExpertCareer);
	}

	@PatchMapping("/my-carrer")
	@Operation(summary = "내 전문가 커리어 수정", description = "전문가 커리어 수정")
	public void updateExpertCareer(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(content = @Content(schema = @Schema(implementation = RequestUpdateExpertCareer.class)))
		@Validated @RequestBody List<RequestUpdateExpertCareer> requestUpdateExpertCareer) {
		expertCareerService.updateExpertCareer(member, requestUpdateExpertCareer);
	}

	@GetMapping("/my-career")
	@ApiResponse(description = "현재 회원 정보(리스트임!!)", content = @Content(schema = @Schema(implementation = ResponseGetExpertCareer.class)))
	@Operation(summary = "내 전문가 커리어 조회(리스트임!!)", description = "내 전문가 커리어 조회(리스트임!!)")
	public ResponseEntity<List<ResponseGetExpertCareer>> getExpertCareer(
		@Parameter(hidden = true) @CurrentMember Member member) {
		return ResponseEntity.ok(expertCareerService.getMyExpertCareer(member));
	}

	@DeleteMapping("/my-career")
	@Operation(summary = "내 전문가 커리어 삭제", description = "전문가 커리어 삭제")
	public void deleteExpertCareer(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(description = "전문가 커리어 아이디") @RequestParam Long expertCareerId) {
		expertCareerService.deleteExpertCareer(member, expertCareerId);
	}
}

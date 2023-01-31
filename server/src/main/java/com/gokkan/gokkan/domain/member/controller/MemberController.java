package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.dto.MemberDto.RequestUpdateDto;
import com.gokkan.gokkan.domain.member.domain.dto.MemberDto.ResponseDto;
import com.gokkan.gokkan.domain.member.domain.dto.MemberDto.ResponseSellerInfo;
import com.gokkan.gokkan.domain.member.service.MemberService;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "회원 컨트롤러", description = "회원 컨트롤러")
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

//	@GetMapping
//	@Operation(summary = "로그인한 회원 정보 조회", description = "로그인한 회원 정보 조회")
//	@ApiResponse(description = "현재 회원 정보", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
//	public ResponseEntity<ResponseDto> getUser() {
//		MemberAdapter principal = (MemberAdapter) SecurityContextHolder.getContext()
//			.getAuthentication()
//			.getPrincipal();
//
//		Member member = principal.getMember();
//
//		return ResponseEntity.ok(ResponseDto.fromEntity(member));
//	}

	@GetMapping
	@Operation(summary = "로그인한 회원 정보 조회", description = "로그인한 회원 정보 조회")
	@ApiResponse(description = "현재 회원 정보", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	public ResponseEntity<ResponseDto> getUser(
		@Parameter(hidden = true) @CurrentMember Member member) {
		log.info("멤버 조회 요청 이름 : " + member.getNickName());

		return ResponseEntity.ok(ResponseDto.fromEntity(member));
	}

	@PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
	public ResponseEntity<Void> updateMember(
		@Parameter(content = @Content(schema = @Schema(implementation = RequestUpdateDto.class)))
		@RequestPart RequestUpdateDto requestUpdateDto,
		@Parameter(description = "프로필 이미지 MultipartFile")
		@RequestPart MultipartFile profileImage,
		@Parameter(hidden = true) @CurrentMember Member member) {
		memberService.updateMember(member, requestUpdateDto, profileImage);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/card")
	@Operation(summary = "카드 정보 수정", description = "카드 정보 수정")
	public ResponseEntity<Void> updateCard(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(description = "카드 번호")
		@RequestParam String cardNumber) {
		memberService.updateCard(member, cardNumber);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/address")
	@Operation(summary = "주소 정보 수정", description = "주소 정보 수정")
	public ResponseEntity<Void> updateAddress(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(description = "주소")
		@RequestParam String address,
		@Parameter(description = "상세 주소")
		@RequestParam String addressDetail) {
		memberService.updateAddress(member, address, addressDetail);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/nickName/duplicate")
	@Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크")
	public ResponseEntity<Boolean> checkDuplicateNickName(
		@Parameter(description = "닉네임")
		@RequestParam String nickName) {
		return ResponseEntity.ok(memberService.checkDuplicateNickName(nickName));
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그아웃")
	public ResponseEntity<Void> logout(
		@Parameter(hidden = true) @CurrentMember Member member) {
		memberService.logout(member);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/seller")
	@Operation(summary = "판매자 정보 조회", description = "판매자 정보 조회")
	@ApiResponse(description = "판매자 정보", content = @Content(schema = @Schema(implementation = ResponseSellerInfo.class)))
	public ResponseEntity<ResponseSellerInfo> getSellerInfo(
		@Parameter(description = "상품 아이디") @RequestParam Long itemId) {
		return ResponseEntity.ok(memberService.getSellerInfo(itemId));
	}
}


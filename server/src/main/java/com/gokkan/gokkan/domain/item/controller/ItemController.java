package com.gokkan.gokkan.domain.item.controller;

import static com.gokkan.gokkan.domain.item.dto.ItemDto.ListResponse;
import static com.gokkan.gokkan.domain.item.dto.ItemDto.Response;
import static com.gokkan.gokkan.domain.item.dto.ItemDto.ResponseForAuction;
import static com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;

import com.gokkan.gokkan.domain.item.service.ItemService;
import com.gokkan.gokkan.domain.item.type.State;
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
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "상품 컨트롤러", description = "상품 임시 저장, 저장 완료, 수정, 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

	private final ItemService itemService;

	@Operation(summary = "상품 생성 완료", description = "상품 생성, Amazon S3에 파일 업로드, 업로드 된 이미지 url 상품에 저장")
	@ApiResponse(responseCode = "200", description = "생성된 상품 반환, 상품 상태 검수중으로 변경, 수정 불가", content = @Content(schema = @Schema(implementation = Response.class)))
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	public ResponseEntity<?> create(
		@Parameter(description = "상품 생성 정보", required = true, content = @Content(schema = @Schema(implementation = UpdateRequest.class)))
		@Validated @RequestPart UpdateRequest request,
		@Parameter(description = "상품 이미지 파일 (여러 파일 업로드 가능)")
		@RequestPart(required = false) List<MultipartFile> imageItemFiles,
		@Parameter(description = "검수 이미지 파일 (여러 파일 업로드 가능)")
		@RequestPart(required = false) List<MultipartFile> imageCheckFiles,
		@Parameter(hidden = true)
		@CurrentMember Member member) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(itemService.create(request, imageItemFiles, imageCheckFiles, member));
	}

	@Operation(summary = "상품 디테일 조회", description = "itemId에 해당하는 상품 디테일 조회, 상품 상태 임시저장/반려 상태는 읽지 못함")
	@ApiResponse(responseCode = "200", description = "조회한 상품 반환", content = @Content(schema = @Schema(implementation = Response.class)))
	@GetMapping("/details")
	public ResponseEntity<?> read(
		@Parameter(description = "상품 아이디", required = true)
		@RequestParam Long itemId) {
		return ResponseEntity.ok(itemService.readDetail(itemId));
	}

	@Operation(summary = "임시 저장 상품 디테일 조회", description = "itemId에 해당하는 임시 저장 상품 디테일 조회")
	@ApiResponse(responseCode = "200", description = "조회한 상품 반환", content = @Content(schema = @Schema(implementation = Response.class)))
	@GetMapping("/details/temp")
	public ResponseEntity<?> tempRead(
		@Parameter(description = "상품 아이디", required = true)
		@RequestParam Long itemId,
		@Parameter(hidden = true)
		@CurrentMember Member member) {
		return ResponseEntity.ok(itemService.readDetailTemp(itemId, member));
	}

	@Operation(summary = "경매 전용 상품 디테일 조회", description = "itemId에 해당하는 경매 전용 상품 디테일 조회")
	@ApiResponse(responseCode = "200", description = "조회한 상품 반환, State = COMPLETE 아니면 에러 발생", content = @Content(schema = @Schema(implementation = ResponseForAuction.class)))
	@GetMapping("/details/auction")
	public ResponseEntity<?> auctionRead(
		@Parameter(description = "상품 아이디", required = true)
		@RequestParam Long itemId) {
		return ResponseEntity.ok(itemService.readDetailAuction(itemId));
	}

	@Operation(summary = "상품 삭제", description = "itemId에 해당하는 상품 삭제")
	@ApiResponse(responseCode = "200", description = "itemId에 해당하는 상품 삭제")
	@DeleteMapping
	public ResponseEntity<?> delete(
		@Parameter(description = "상품 아이디", required = true)
		@RequestParam Long itemId,
		@Parameter(hidden = true)
		@CurrentMember Member member) {
		return ResponseEntity.ok(itemService.delete(itemId, member));
	}

	@Operation(summary = "상품 수정", description = "상품 수정, Amazon S3에 파일 업로드, 수정된 이미지 url 상품에 저장")
	@ApiResponse(responseCode = "201", description = "수정된 상품 반환, 상품은 임시 저장상태", content = @Content(schema = @Schema(implementation = Response.class)))
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	public ResponseEntity<?> update(
		@Parameter(description = "상품 수정 정보", required = true, content = @Content(schema = @Schema(implementation = UpdateRequest.class)))
		@RequestPart UpdateRequest request,
		@Parameter(description = "상품 이미지 파일 (여러 파일 업로드 가능)")
		@RequestPart(required = false) List<MultipartFile> imageItemFiles,
		@Parameter(description = "검수 이미지 파일 (여러 파일 업로드 가능)")
		@RequestPart(required = false) List<MultipartFile> imageCheckFiles,
		@Parameter(hidden = true)
		@CurrentMember Member member) {

		return ResponseEntity.ok(
			itemService.update(request, imageItemFiles, imageCheckFiles, member));
	}

	@Operation(summary = "상품 임시 생성", description = "상품 임시 생성, 상품 아이디만 반환")
	@ApiResponse(responseCode = "201", description = "상품 임시 생성, 상품 아이디만 반환")
	@PostMapping("/temp")
	@Transactional
	public ResponseEntity<?> create(
		@Parameter(hidden = true)
		@CurrentMember Member member) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(itemService.createTemporary(member));
	}


	@Operation(summary = "나의 상품 list 조회", description = "요청한 states 에 맞는 상품 list 반환")
	@ApiResponse(responseCode = "200", description = "상품 주요 정보만 반환", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	@GetMapping("/my-items")
	public ResponseEntity<?> myItems(
		@Parameter(hidden = true)
		@CurrentMember Member member,
		@Parameter(description = "상품 상태 list")
		@RequestParam(required = false) List<State> states,
		@ParameterObject
			Pageable pageable) {
		return ResponseEntity.ok(itemService.myItems(member, states, pageable));
	}

	@Operation(summary = "전문가가 평가할 상품 list 조회", description = "전문가 스타일에 맞는 상품 list 반환")
	@ApiResponse(responseCode = "200", description = "상품 주요 정보만 반환", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	@GetMapping("/expert-items")
	public ResponseEntity<?> items(
		@Parameter(hidden = true)
		@CurrentMember Member member,
		@ParameterObject
			Pageable pageable) {
		return ResponseEntity.ok(itemService.itemsForExport(member, pageable));
	}
}

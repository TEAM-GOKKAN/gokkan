//package com.gokkan.gokkan.domain.image.controller;
//
//import com.gokkan.gokkan.domain.image.domain.ImageCheck;
//import com.gokkan.gokkan.domain.image.service.AwsS3Service;
//import com.gokkan.gokkan.domain.image.service.ImageCheckService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@Tag(name = "검수 이미지 컨트롤러", description = "검수용 이미지 저장, 읽기, 수정, 삭제")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/ImageChecks")
//public class ImageCheckController {
//
//	private final ImageCheckService imageCheckService;
//
//	private final AwsS3Service awsS3Service;
//
//	@Operation(summary = "검수 이미지 생성", description = "검수 이미지 생성, Amazon S3에 파일 업로드, 업로드 된 이미지 url 장소에 저장")
//	@ApiResponse(responseCode = "201", description = "생성된 검수 이미지 반환", content = @Content(schema = @Schema(implementation = ImageCheck.class)))
//	@PostMapping("")
//	public ResponseEntity<?> save(
//		@Parameter(description = "Image (여러 파일 업로드 가능)", required = true)
//		@RequestPart List<MultipartFile> multipartFiles) {
//		return ResponseEntity.ok(
//			imageCheckService.create(awsS3Service.save(multipartFiles)));
//	}
//
//	@Operation(summary = "검수 이미지 삭제", description = "검수 이미지 삭제, Amazon S3에 파일 삭제")
//	@ApiResponse(responseCode = "200", description = "검수 이미지 삭제, Amazon S3에 파일 삭제")
//	@DeleteMapping("")
//	public ResponseEntity<?> delete(@RequestParam Long imageCheckId) {
//		return ResponseEntity.ok(imageCheckService.delete(imageCheckId));
//	}
//}

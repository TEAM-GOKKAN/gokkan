package com.gokkan.gokkan.domain.image.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements ErrorCode {

	EMPTY_FILE(HttpStatus.BAD_REQUEST, "저장할 이미지가 없습니다."),
	EMPTY_URL(HttpStatus.BAD_REQUEST, "저장할 이미지 url이 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패하였습니다."),
	MISMATCH_FILE_TYPE(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일입니다. 확장자 명이 존재하지 않습니다."),
	NOT_DELETED_IMAGE(HttpStatus.BAD_REQUEST, "오류가 발생하여 이미지가 삭제되지 않았습니다."),
	INVALID_FORMAT_FILE(HttpStatus.BAD_REQUEST, "png, jpg, jpeg 형식의 파일이 아닙니다."),
	INVALID_FORMAT_URL(HttpStatus.BAD_REQUEST, "잘못된 이미지 url입니다."),
	NOT_FOUND_IMAGE_ITEM(HttpStatus.BAD_REQUEST, "해당 아이디의 상품 이미지를 찾을 수 없습니다."),
	NOT_FOUND_IMAGE_CHECK(HttpStatus.BAD_REQUEST, "해당 아이디의 검수 이미지를 찾을 수 없습니다."),
	TOO_MANY_IMAGE(HttpStatus.BAD_REQUEST, "이미지가 너무 많습니다. 항목당 5개 이하로 보내주세요"),
	IMAGE_SIZE_TOO_BIG(HttpStatus.BAD_REQUEST, "이미지 용량이 너무 큽니다. 이미지 하나당 5MB 이하만 가능합니다.");


	private final HttpStatus httpStatus;
	private final String message;
}

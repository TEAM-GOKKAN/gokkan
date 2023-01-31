package com.gokkan.gokkan.global.exception.handler;

import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.global.exception.dto.ErrorDto.MessageError;
import com.gokkan.gokkan.global.exception.errorcode.CommonErrorCode;
import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.exception.response.ErrorResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<Object> handleQuizException(final RestApiException e) {
		final ErrorCode errorCode = e.getErrorCode();
		log.warn("RestApiException : " + errorCode.getHttpStatus().value() + " ("
			+ errorCode.getMessage() + ")");
		return handleExceptionInternal(errorCode);
	}

	@MessageExceptionHandler
	@SendToUser(value = "/queue/error")
	public MessageError handleMessageException(final RestApiException e) {
		final ErrorCode errorCode = e.getErrorCode();
		log.warn("RestApiException : " + errorCode.getHttpStatus().value() + " ("
			+ errorCode.getMessage() + ")");
		return MessageError.builder()
			.status(errorCode.getHttpStatus().value())
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
		log.warn("handleIllegalArgument", e);
		final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(errorCode, e.getMessage());
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		final MethodArgumentNotValidException e,
		final HttpHeaders headers,
		final HttpStatus status,
		final WebRequest request) {
		log.warn("handleIllegalArgument", e);
		final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(e, errorCode);
	}

//    @ExceptionHandler({ConstraintViolationException.class})
//    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException e) {
//        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
//        return handleExceptionInternal(e, errorCode);
//    }

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(
		final MaxUploadSizeExceededException e) {
		log.warn("RestApiException : " + HttpStatus.BAD_REQUEST.value() + " ("
			+ e.getMessage() + ")");
		final ErrorCode errorCode = ImageErrorCode.IMAGE_SIZE_TOO_BIG;
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(final Exception ex) {
		log.error("handleAllException", ex);
		final ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(errorCode);
	}

	private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode));
	}

	private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode,
		final String message) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode, message));
	}

	private ErrorResponse makeErrorResponse(final ErrorCode errorCode, final String message) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(message)
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(final BindException e,
		final ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(e, errorCode));
	}

	private ErrorResponse makeErrorResponse(final BindException e, final ErrorCode errorCode) {
		final List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ErrorResponse.ValidationError::of)
			.collect(Collectors.toList());

		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.errors(validationErrorList)
			.build();
	}

//    private ResponseEntity<Object> handleExceptionInternal(final ConstraintViolationException e, final ErrorCode errorCode) {
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(makeErrorResponse(e, errorCode));
//    }

//    private ErrorResponse makeErrorResponse(final ConstraintViolationException e, final ErrorCode errorCode) {
//        final List<ErrorResponse.ValidationError> validationErrorList = e.getConstraintViolations()
//                .stream()
//                .map(ErrorResponse.ValidationError::of)
//                .collect(Collectors.toList());
//
//        return ErrorResponse.builder()
//                .code(errorCode.name())
//                .message(errorCode.getMessage())
//                .errors(validationErrorList)
//                .build();
//    }

}

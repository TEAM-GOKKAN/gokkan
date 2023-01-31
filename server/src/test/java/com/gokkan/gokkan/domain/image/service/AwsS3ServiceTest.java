package com.gokkan.gokkan.domain.image.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.amazonaws.services.s3.AmazonS3;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AwsS3ServiceTest {

	private final String png = "png";
	@Mock
	private AmazonS3 amazonS3;
	@InjectMocks
	private AwsS3Service awsS3Service;

	@DisplayName("01_00. save success")
	@Test
	public void test_01_00() throws IOException {
		//given

		//when
		String url = awsS3Service.save(getMultipartFiles(png));
		verify(amazonS3, times(1)).putObject(any());

		//then
		assertEquals(url.substring(url.lastIndexOf(".")), "." + png);
	}

	@DisplayName("01_01. save fail EMPTY_FILE")
	@Test
	public void test_01_01() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> awsS3Service.save(getMultipartFiles("jpg")));

		//then
		assertEquals(restApiException.getErrorCode(), ImageErrorCode.EMPTY_FILE);
	}

	@DisplayName("01_02. save fail INVALID_FORMAT_FILE")
	@Test
	public void test_01_02() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> awsS3Service.save(getMultipartFiles("txt")));

		//then
		assertEquals(restApiException.getErrorCode(), ImageErrorCode.INVALID_FORMAT_FILE);
	}

	@DisplayName("02_00. check success")
	@Test
	public void test_02_00() {
		//given

		//when
		assertDoesNotThrow(() -> awsS3Service.check(getMultipartFiles(png)));
		//then
	}

	@DisplayName("02_01. check fail INVALID_FORMAT_FILE")
	@Test
	public void test_02_01() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> awsS3Service.check(getMultipartFiles("txt")));

		//then
		assertEquals(restApiException.getErrorCode(), ImageErrorCode.INVALID_FORMAT_FILE);
	}

	@DisplayName("03_00. checkImageCount success")
	@Test
	public void test_03_00() {
		//given

		//when
		assertDoesNotThrow(() -> awsS3Service.checkImageCount(List.of(getMultipartFiles(png)), 4));

		//then
	}

	@DisplayName("03_01. checkImageCount fail TOO_MANY_IMAGE")
	@Test
	public void test_03_01() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> awsS3Service.checkImageCount(List.of(getMultipartFiles(png)), 5));

		//then
		assertEquals(restApiException.getErrorCode(), ImageErrorCode.TOO_MANY_IMAGE);
	}

	private MultipartFile getMultipartFiles(String extension) throws IOException {

		String file = String.format("%d.%s", 1, extension);
		FileInputStream fis = new FileInputStream("src/main/resources/testImages/" + file);
		return new MockMultipartFile("1", file, extension, fis);
	}

}
package com.gokkan.gokkan.domain.image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.dto.ImageDto;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageCheckServiceTest {

	static ArgumentCaptor<ImageCheck> imageCheckCaptor = ArgumentCaptor.forClass(ImageCheck.class);
	static List<String> urls = List.of("u1", "u2", "u3");
	String url1 = "test url1";
	String url2 = "test url2";
	String url3 = "test url3";
	ImageDto.UpdateRequest updateRequest1 = ImageDto.UpdateRequest.builder().imageId(1L).build();
	ImageDto.UpdateRequest updateRequest2 = ImageDto.UpdateRequest.builder().imageId(2L).build();
	ImageCheck imageCheck1 = ImageCheck.builder().id(1L).url(url1).build();
	ImageCheck imageCheck2 = ImageCheck.builder().id(2L).url(url2).build();
	ImageCheck imageCheck3 = ImageCheck.builder().id(3L).url(url3).build();
	@Mock
	private ImageCheckRepository imageCheckRepository;
	@Mock
	private AwsS3Service awsS3Service;
	@InjectMocks
	private ImageCheckService imageCheckService;

	private static ImageCheck getImageCheck(String url) {
		return ImageCheck.builder()
			.url(url)
			.build();
	}

	@DisplayName("01_00. save success")
	@Test
	public void test_01_00() {
		//given
		for (String url : urls) {
			ImageCheck imageCheck = getImageCheck(url);
			lenient().when(imageCheckRepository.save(imageCheck)).thenReturn(imageCheck);
		}

		//when
		List<ImageCheck> imageChecks = imageCheckService.create(urls);
		verify(imageCheckRepository, times(0)).save(imageCheckCaptor.capture());

		//then
		for (int i = 0; i < urls.size(); i++) {
			assertEquals(imageChecks.get(i).getUrl(), urls.get(i));
		}
	}

	@DisplayName("02_00. delete success")
	@Test
	public void test_02_00() {
		//given
		given(imageCheckRepository.findById(1L))
			.willReturn(Optional.of(getImageCheck("test")));

		//when
		boolean deleted = imageCheckService.delete(1L);
		verify(imageCheckRepository, times(1)).delete(any());
		verify(awsS3Service, times(1)).delete(any());

		//then
		assertTrue(deleted);
	}

	@DisplayName("02_01. delete fail not found")
	@Test
	public void test_02_01() {
		//given
		given(imageCheckRepository.findById(1L))
			.willReturn(Optional.empty());

		//when
		RestApiException imageException = assertThrows(RestApiException.class,
			() -> imageCheckService.delete(1L));
		verify(imageCheckRepository, times(0)).delete(any());
		verify(awsS3Service, times(0)).delete(any());

		//then
		assertEquals(imageException.getErrorCode(), ImageErrorCode.NOT_FOUND_IMAGE_CHECK);
	}

	@DisplayName("03_00. checkImageCheckDeleted success ")
	@Test
	public void test_03_00() {
		//given

		//when
		List<ImageCheck> imageChecks = imageCheckService.checkImageCheckDeleted(
			List.of(updateRequest1, updateRequest2), List.of(imageCheck1, imageCheck2)
		);

		//then
		assertEquals(imageChecks.size(), 2);
		assertEquals(imageChecks.get(0).getUrl(), url1);
		assertEquals(imageChecks.get(1).getUrl(), url2);
	}

	@DisplayName("03_01. checkImageCheckDeleted success ")
	@Test
	public void test_03_01() {
		//given

		//when
		List<ImageCheck> imageChecks = imageCheckService.checkImageCheckDeleted(
			List.of(updateRequest1, updateRequest2), List.of(imageCheck1, imageCheck2, imageCheck3)
		);

		//then
		assertEquals(imageChecks.size(), 2);
		assertEquals(imageChecks.get(0).getUrl(), url1);
		assertEquals(imageChecks.get(1).getUrl(), url2);
	}

	@DisplayName("03_02. checkImageCheckDeleted success ")
	@Test
	public void test_03_02() {
		//given

		//when
		List<ImageCheck> imageChecks = imageCheckService.checkImageCheckDeleted(
			new ArrayList<>(), List.of(imageCheck1, imageCheck2, imageCheck3)
		);

		//then
		assertEquals(imageChecks.size(), 0);
	}
}
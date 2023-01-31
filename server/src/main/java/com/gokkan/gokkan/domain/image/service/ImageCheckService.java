package com.gokkan.gokkan.domain.image.service;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.dto.ImageDto.UpdateRequest;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ImageCheckService {

	private final ImageCheckRepository imageCheckRepository;
	private final AwsS3Service awsS3Service;


	public List<ImageCheck> create(List<String> urls) {
		log.info("create List<String> size : " + urls.size());
		List<ImageCheck> imageChecks = new ArrayList<>();
		for (String url : urls) {
			imageChecks.add(
				ImageCheck.builder()
					.url(url)
					.build()
			);
		}

		return imageChecks;
	}

	public boolean delete(Long imageCheckId) {
		log.info("delete imageCheckId : " + imageCheckId);
		ImageCheck imageCheck = getImageCheck(imageCheckId);
		String url = imageCheck.getUrl();
		imageCheckRepository.delete(imageCheck);
		awsS3Service.delete(url);

		return true;
	}

	public boolean delete(ImageCheck imageCheck) {
		log.info("delete imageCheck url : " + imageCheck.getUrl());
		String url = imageCheck.getUrl();
		imageCheckRepository.delete(imageCheck);
		awsS3Service.delete(url);

		return true;
	}


	public void deleteAllImageItems(List<ImageCheck> saved) {
		log.info("deleteAllImageItems List<ImageCheck> : " + saved.size());
		for (ImageCheck imageCheck : saved) {
			delete(imageCheck);
		}
	}


	private ImageCheck getImageCheck(Long imageCheckId) {
		log.info("getImageCheck imageCheckId : " + imageCheckId);
		return imageCheckRepository.findById(imageCheckId)
			.orElseThrow(() -> new RestApiException(ImageErrorCode.NOT_FOUND_IMAGE_CHECK));
	}

	public List<ImageCheck> checkImageCheckDeleted(List<UpdateRequest> urls,
		List<ImageCheck> saved) {
		log.info("checkImageCheckDeleted");
		List<ImageCheck> imageChecks = new ArrayList<>();
		boolean[] deleted = new boolean[saved.size()];
		int deletedCount = saved.size() - urls.size();

		if (deletedCount == 0) {
			return saved;
		} else if (deletedCount == saved.size()) {
			deleteAllImageItems(saved);
			return new ArrayList<>();
		} else {
			int nextStartIndex = 0;

			for (ImageCheck imageCheck : saved) {
				for (int j = nextStartIndex; j < urls.size(); j++) {
					if (imageCheck.getId().equals(urls.get(j).getImageId())) {
						deleted[j] = true;
						nextStartIndex = j;
						break;
					}

				}
			}

			for (int i = 0; i < deleted.length; i++) {
				if (deleted[i]) {
					imageChecks.add(saved.get(i));
				} else {
					delete(saved.get(i));
				}
			}
			return imageChecks;
		}
	}
}

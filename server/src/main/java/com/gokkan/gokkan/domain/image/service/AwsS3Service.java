package com.gokkan.gokkan.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.baseUrl}")
	private String baseUrl;

	public String save(MultipartFile multipartFile) {
		log.info("save multipartFile : " + multipartFile.getOriginalFilename());
		if (multipartFile.isEmpty()) {
			log.error("save multipartFile : " + multipartFile.getOriginalFilename());
			throw new RestApiException(ImageErrorCode.EMPTY_FILE);
		}

		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3.putObject(
				new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (IOException e) {
			log.error("putObject multipartFile : " + multipartFile.getOriginalFilename());
			throw new RestApiException(ImageErrorCode.INTERNAL_SERVER_ERROR);
		}

		return baseUrl + fileName;
	}

	public void check(MultipartFile multipartFile) {
		log.info("check multipartFile : " + multipartFile.getOriginalFilename());
		String filename = multipartFile.getOriginalFilename();
		if (filename != null) {
			checkName(filename);
		} else {
			log.error("check multipartFile : " + multipartFile.getOriginalFilename());
			throw new RestApiException(ImageErrorCode.MISMATCH_FILE_TYPE);
		}
	}

	public List<String> save(List<MultipartFile> multipartFiles) {
		log.info("save List<multipartFile> size : " + multipartFiles.size());
		if (multipartFiles.get(0).isEmpty()) {
			return new ArrayList<>();
		}

		List<String> urls = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			urls.add(save(multipartFile));
		}

		return urls;
	}

	public void check(List<MultipartFile> multipartFiles) {
		log.info("check List<multipartFile> size : " + multipartFiles.size());
		if (multipartFiles.get(0).isEmpty()) {
			log.info("check List<multipartFile> empty");
			return;
		}
		for (MultipartFile multipartFile : multipartFiles) {
			check(multipartFile);
		}
	}

	public void checkImageCount(List<MultipartFile> multipartFiles, int savedCount) {
		log.info("checkImageCount List<multipartFile> size : " + multipartFiles.size()
			+ ", savedImageCount : " + savedCount);
		int multipartFiesSize = multipartFiles.size() == 1 ?
			(multipartFiles.get(0).isEmpty() ? -1 : 1)
			: multipartFiles.size();

		if (multipartFiesSize == -1) {
			return;
		}

		if (multipartFiesSize + savedCount > 5) {
			log.error("checkImageCount totalCount : " + multipartFiesSize + savedCount);
			throw new RestApiException(ImageErrorCode.TOO_MANY_IMAGE);
		}
	}

	public boolean delete(String url) {
		log.info("delete url : " + url);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucket, url.replaceAll(baseUrl, "")));
		} catch (Error e) {
			log.error("delete url : " + url);
			throw new RestApiException(ImageErrorCode.NOT_DELETED_IMAGE);
		}
		return true;
	}

	private void checkName(String fileName) {
		log.info("checkName fileName : " + fileName);
		try {
			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg")) {
				return;
			}
			log.error("checkName fileName : " + fileName);
			throw new RestApiException(ImageErrorCode.INVALID_FORMAT_FILE);
		} catch (StringIndexOutOfBoundsException e) {
			log.error("checkName fileName : " + fileName);
			throw new RestApiException(ImageErrorCode.MISMATCH_FILE_TYPE);
		}
	}

	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {
		log.info("getFileExtension fileName : " + fileName);
		try {
			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg")) {
				return extension;
			}
			log.error("getFileExtension fileName : " + fileName);
			throw new RestApiException(ImageErrorCode.INVALID_FORMAT_FILE);
		} catch (StringIndexOutOfBoundsException e) {
			log.error("getFileExtension fileName : " + fileName);
			throw new RestApiException(ImageErrorCode.MISMATCH_FILE_TYPE);
		}
	}
}
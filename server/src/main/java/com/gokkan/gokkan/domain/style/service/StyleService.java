package com.gokkan.gokkan.domain.style.service;

import static com.gokkan.gokkan.domain.style.dto.StyleDto.Response;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.dto.StyleDto.CreateRequest;
import com.gokkan.gokkan.domain.style.dto.StyleDto.UpdateRequest;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StyleService {

	private final StyleRepository styleRepository;

	public Response create(CreateRequest request) {
		log.info("create style name : " + request.getName());
		duplicateCheck(request.getName());
		return Response.toResponse(
			styleRepository.save(
				Style.builder()
					.name(request.getName())
					.build()));
	}

	@Transactional(readOnly = true)
	public Response read(String name) {
		log.info("read style name : " + name);
		return Response.toResponse(getStyleByName(name));
	}

	public boolean delete(String name) {
		log.info("delete style name : " + name);
		styleRepository.delete(getStyleByName(name));
		return true;
	}

	public Response update(UpdateRequest request) {
		log.info("update style name : " + request.getName());
		duplicateCheck(request.getName());
		Style style = getStyleById(request);
		style.setName(request.getName());
		return Response.toResponse(styleRepository.save(style));
	}

	private Style getStyleByName(String name) {
		log.info("getStyleByName style name : " + name);
		return styleRepository.findByName(name).orElseThrow(() -> {
			log.error("getStyleByName style name : " + name);
			return new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
		});
	}

	private Style getStyleById(UpdateRequest request) {
		log.info("getStyleById style name : " + request.getName());
		return styleRepository.findById(request.getId()).orElseThrow(() -> {
			log.error("getStyleById style name : " + request.getName());
			return new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
		});
	}

	private void duplicateCheck(String name) {
		log.info("duplicateCheck style name : " + name);
		if (styleRepository.existsByName(name)) {
			log.error("duplicateCheck style name : " + name);
			throw new RestApiException(StyleErrorCode.DUPLICATE_STYLE);
		}
	}
}

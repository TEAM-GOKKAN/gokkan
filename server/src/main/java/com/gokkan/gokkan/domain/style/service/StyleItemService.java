package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
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
public class StyleItemService {

	private final StyleItemRepository styleItemRepository;
	private final StyleRepository styleRepository;

	public List<StyleItem> createNotDuplicate(List<String> names, List<StyleItem> saved) {
		log.info("createNotDuplicate");
		for (String name : names) {
			styleExistCheck(name);
		}

		boolean[] deleted = new boolean[saved.size()];        // true -> 기존 save 에서 삭제 안된것 			false ->  기존 save 에서 삭제한것
		boolean[] duplicate = new boolean[names.size()];    // true -> 저장 된 것과 중복된 것 	false -> 중복 안되어서 StyleItem 생성 할 것

		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			for (int j = 0; j < saved.size(); j++) {
				if (name.equals(saved.get(j).getName())) {
					duplicate[i] = true;
					deleted[j] = true;
					break;
				}
			}
		}

		List<StyleItem> styleItems = new ArrayList<>();
		for (int i = 0; i < deleted.length; i++) {
			if (deleted[i]) {
				styleItems.add(saved.get(i));
			} else {
				styleItemRepository.delete(saved.get(i));
			}
		}

		for (int i = 0; i < duplicate.length; i++) {
			if (!duplicate[i]) {
				styleItems.add(StyleItem.builder()
					.style(getStyle(names.get(i)))
					.name(names.get(i))
					.build());
			}
		}

		return styleItems;
	}

	private void styleExistCheck(String name) {
		log.info("styleExistCheck style name : " + name);
		if (!styleRepository.existsByName(name)) {
			log.error("styleExistCheck style name : " + name);
			throw new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
		}
	}

	@Transactional
	public StyleItem update(Long id, String name) {
		log.info("update style item id : " + id);
		StyleItem styleItem = getStyleItem(id);
		styleItem.setStyle(getStyle(name));
		styleItem.setName(name);
		return styleItemRepository.save(styleItem);
	}

	private StyleItem getStyleItem(Long id) {
		log.info("getStyleItem style item id : " + id);
		return styleItemRepository.findById(id)
			.orElseThrow(() -> {
				log.error("getStyleItem style item id : " + id);
				return new RestApiException(StyleErrorCode.NOT_FOUND_STYLE_ITEM);
			});
	}

	private Style getStyle(String name) {
		log.info("getStyle style name : " + name);
		return styleRepository.findByName(name)
			.orElseThrow(() -> {
				log.error("getStyle style name : " + name);
				return new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
			});
	}

	public void deleteAll(List<StyleItem> styleItems) {
		log.info("deleteAll List<StyleItem> size : " + styleItems.size());
		styleItemRepository.deleteAll(styleItems);
	}
}

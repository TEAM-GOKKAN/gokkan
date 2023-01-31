package com.gokkan.gokkan.domain.category.service;

import static com.gokkan.gokkan.domain.category.dto.CategoryDto.CreateRequest;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.Response;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.UpdateRequest;
import com.gokkan.gokkan.domain.category.exception.CategoryErrorCode;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	private void checkParentNameAndChildName(String name, String parent) {
		log.info("checkParentNameAndChildName name : " + name + " parent : " + parent);
		if (name.equals(parent)) {
			log.error("checkParentNameAndChildName name : " + name);
			throw new RestApiException(CategoryErrorCode.CAN_NOT_SAME_PARENT_NAME);
		}
	}

	@Transactional
	public Response create(CreateRequest request) {
		log.info("create name : " + request.getName() + " parent : " + request.getParent());
		checkParentNameAndChildName(request.getName(), request.getParent());
		duplicateCheck(request.getName());
		Category parent;
		Category category = request.toEntity();

		if (request.getParent() == null) {
			log.info("create make root");
			parent = categoryRepository.findByName("root")
				.orElseGet(() -> categoryRepository.save(Category.builder()
					.level(0)
					.parent(null)
					.children(new ArrayList<>())
					.name("root")
					.build()));
		} else {
			log.info("create make not root");
			parent = getCategoryByName(request.getParent(), true);
		}

		category.setLevel(parent.getLevel() + 1);
		Category.addRelation(parent, category);

		return Response.toResponse(categoryRepository.save(category));
	}

	@Transactional(readOnly = true)
	public Response read(String name) {
		log.info("read name : " + name);
		return Response.toResponse(getCategoryByName(name, false));
	}

	@Transactional
	public boolean delete(String name) {
		log.info("delete name : " + name);
		categoryRepository.delete(getCategoryByName(name, false));
		return true;
	}

	@Transactional
	public Response update(UpdateRequest request) {
		log.info("update name : " + request.getName() + " parent : " + request.getParent());
		checkParentNameAndChildName(request.getName(), request.getParent());
		Category category = getCategoryById(request.getId());
		if (!category.getName().equals(request.getName())) {
			duplicateCheck(request.getName());
		}

		if (!category.getParent().getName().equals(request.getParent())) {
			Category updateParent = getCategoryByName(request.getParent(), true);

			category.setParent(updateParent);
			category.setLevel(updateParent.getLevel() + 1);
			Category.addRelation(updateParent, category);
		}

		category.setName(request.getName());

		return Response.toResponse(categoryRepository.save(category));
	}

	@Transactional(readOnly = true)
	public Response readAll() {
		log.info("readAll");
		return Response.all(getCategory("root"));
	}

	public Category getCategory(String categoryName) {
		log.info("getCategory name : " + categoryName);
		return getCategoryByName(categoryName, false);
	}

	private void duplicateCheck(String name) {
		log.info("duplicateCheck name : " + name);
		if (categoryRepository.existsByName(name)) {
			log.error("duplicateCheck name : " + name);
			throw new RestApiException(CategoryErrorCode.DUPLICATED_CATEGORY);
		}
	}

	private Category getCategoryById(long id) {
		log.info("getCategoryById id : " + id);
		return categoryRepository.findById(id)
			.orElseThrow(() -> {
				log.error("getCategoryById id : " + id);
				return new RestApiException(CategoryErrorCode.NOT_FOUND_CATEGORY);
			});
	}

	private Category getCategoryByName(String name, boolean parent) {
		if (name == null || name.equals("")) {
			return categoryRepository.findByName("").orElseGet(
				() -> categoryRepository.save(Category.builder().name("").level(0).build()));
		}
		return parent ?
			categoryRepository.findByName(name)
				.orElseThrow(
					() -> {
						log.error("getCategoryByName name : " + name);
						return new RestApiException(CategoryErrorCode.NOT_FOUND_PARENT_CATEGORY);
					}
				)
			: categoryRepository.findByName(name)
				.orElseThrow(
					() -> {
						log.error("getCategoryByName name : " + name);
						return new RestApiException(CategoryErrorCode.NOT_FOUND_CATEGORY);
					}
				);
	}
}

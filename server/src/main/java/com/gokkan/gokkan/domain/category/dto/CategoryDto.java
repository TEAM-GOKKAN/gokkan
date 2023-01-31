package com.gokkan.gokkan.domain.category.dto;

import com.gokkan.gokkan.domain.category.domain.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CategoryDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class CreateRequest {

		@NotNull(message = "카테고리 name 은 null 일 수 없습니다.")
		private String name;

		private String parent;


		public Category toEntity() {
			return Category.builder()
				.name(this.name)
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class UpdateRequest {

		@NotNull(message = "카테고리 id 은 null 일 수 없습니다.")
		private Long id;

		@NotNull(message = "카테고리 name 은 null 일 수 없습니다.")
		private String name;

		@NotNull(message = "카테고리 parent 은 null 일 수 없습니다.")
		private String parent;

	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class Response {

		private Long id;

		private String name;

		private String parent;

		private List<Response> children;

		public static Response toResponse(Category category) {
			return Response.builder()
				.id(category.getId())
				.name(category.getName())
				.parent(category.getParent() == null ?
					"root" : category.getParent().getName())
				.children(category.getChildren() == null ? new ArrayList<>() :
					category.getChildren().stream().map(Response::toResponse)
						.collect(Collectors.toList()))
				.build();
		}

		public static Response all(Category category) {
			return Response.builder()
				.id(category.getId())
				.name(category.getName())
				.parent(category.getParent() == null ?
					"root" : category.getParent().getName())
				.children(category.getChildren()
					.stream().map(Response::all).collect(Collectors.toList()))
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class ResponseForItem {

		private String name;

		private List<ResponseForItem> children;

		public static ResponseForItem toResponseForItem(Category category,
			List<ResponseForItem> child) {
			ResponseForItem response = ResponseForItem.builder()
				.name(category.getName())
				.children(new ArrayList<>())
				.build();

			if (child != null) {
				response.children.addAll(child);
			}
			return response;
		}

		public static ResponseForItem getResponseForItem(Category category,
			List<ResponseForItem> child) {
			Category parent = category.getParent();
			if (parent == null || parent.getName().equals("root")) {
				return toResponseForItem(category, child);
			}

			return getResponseForItem(parent,
				new ArrayList<>(List.of(toResponseForItem(category, child))));
		}

	}
}

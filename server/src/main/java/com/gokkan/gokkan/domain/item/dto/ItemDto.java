package com.gokkan.gokkan.domain.item.dto;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.dto.CategoryDto;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.dto.ImageDto;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class ItemDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class UpdateRequest {

		@NotNull(message = "itemId 는 null 일 수 없습니다.")
		private Long itemId;
		private String name;

		private long startPrice;

		@NotNull(message = "category 는 null 일 수 없습니다.")
		private String category;

		private List<ImageDto.UpdateRequest> imageItemUrls;

		private List<ImageDto.UpdateRequest> imageCheckUrls;

		@NotNull(message = "styles 는 null 일 수 없습니다.")
		private List<String> styles;

		@NotNull(message = "width 는 null 일 수 없습니다.")
		private Long width;
		@NotNull(message = "depth 는 null 일 수 없습니다.")
		private Long depth;
		@NotNull(message = "height 는 null 일 수 없습니다.")
		private Long height;
		@NotNull(message = "material 는 null 일 수 없습니다.")
		private String material;

		@NotNull(message = "conditionGrade 는 null 일 수 없습니다.")
		private String conditionGrade;
		@NotNull(message = "conditionDescription 는 null 일 수 없습니다.")
		private String conditionDescription;
		@NotNull(message = "text 는 null 일 수 없습니다.")
		private String text;

		@NotNull(message = "designer 는 null 일 수 없습니다.")
		private String designer;
		@NotNull(message = "brand 는 null 일 수 없습니다.")
		private String brand;
		private Integer productionYear;

		public Item toItem(Item item, Category category) {
			return Item.builder()
				.id(item.getId())
				.member(item.getMember())
				.name(this.name)
				.category(category)
				.startPrice(this.startPrice)
				.width(this.width)
				.depth(this.depth)
				.height(this.height)
				.material(this.material)
				.conditionGrade(this.conditionGrade)
				.conditionDescription(this.conditionDescription)
				.text(this.text)
				.designer(this.designer)
				.brand(this.brand)
				.productionYear(this.productionYear)
				.state(item.getState())
				.created(item.getCreated())
				.updated(LocalDateTime.now())
				.imageItems(item.getImageItems())
				.imageChecks(item.getImageChecks())
				.styleItems(item.getStyleItems())
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class Response {

		private Long id;
		private String itemNumber;

		private String name;
		private String thumbnail;

		private long startPrice;

		private String state;

		private Long width;
		private Long depth;
		private Long height;
		private String material;

		private String conditionGrade;
		private String conditionDescription;
		private String text;

		private String designer;
		private String brand;
		private int productionYear;

		private String writer;
		private CategoryDto.ResponseForItem category;
		private List<ImageDto.Response> imageItemUrls;
		private List<ImageDto.Response> imageCheckUrls;
		private List<String> styles;

		private LocalDateTime created;
		private LocalDateTime updated;

		public static Response toResponse(Item item) {
			List<ImageItem> imageItems = item.getImageItems();
			List<ImageCheck> imageChecks = item.getImageChecks();
			List<StyleItem> styleItems = item.getStyleItems();
			Category category = item.getCategory();
			return Response.builder()
				.id(item.getId())
				.itemNumber(
					item.getCreated().format(DateTimeFormatter.BASIC_ISO_DATE) + item.getId())
				.name(item.getName() == null ? "" : item.getName())
				.thumbnail(item.getThumbnail() == null ? "" : item.getThumbnail())
				.startPrice(item.getStartPrice())
				.state(item.getState().getDescription())
				.width(item.getWidth())
				.depth(item.getDepth())
				.height(item.getHeight())
				.material(item.getMaterial() == null ? "" : item.getMaterial())
				.conditionGrade(item.getConditionGrade() == null ? "" : item.getConditionGrade())
				.conditionDescription(
					item.getConditionDescription() == null ? "" : item.getConditionDescription())
				.text(item.getText() == null ? "" : item.getText())
				.designer(item.getDesigner() == null ? "" : item.getDesigner())
				.brand(item.getBrand() == null ? "" : item.getBrand())
				.productionYear(item.getProductionYear())
				.created(item.getCreated())
				.updated(item.getUpdated())
				.writer(item.getMember().getNickName() == null ? "" :
					item.getMember().getNickName())
				.category(category == null ? CategoryDto.ResponseForItem.builder().build() :
					CategoryDto.ResponseForItem.getResponseForItem(category, null))
				.imageItemUrls(imageItems == null ? new ArrayList<>() :
					imageItems.stream()
						.map(x -> ImageDto.Response.builder()
							.id(x.getId())
							.url(x.getUrl())
							.build())
						.collect(Collectors.toList()))
				.imageCheckUrls(imageChecks == null ? new ArrayList<>() :
					imageChecks.stream()
						.map(x -> ImageDto.Response.builder()
							.id(x.getId())
							.url(x.getUrl())
							.build())
						.collect(Collectors.toList()))
				.styles(styleItems == null ? new ArrayList<>() :
					styleItems.stream().map(StyleItem::getName)
						.collect(Collectors.toList()))
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class ResponseForAuction {

		private Long id;
		private String itemNumber;

		private String name;
		private String thumbnail;

		private long startPrice;
		private Long width;
		private Long depth;
		private Long height;
		private String material;

		private String conditionGrade;
		private String conditionDescription;
		private String text;

		private String designer;
		private String brand;
		private int productionYear;

		private String writer;
		private CategoryDto.ResponseForItem category;
		private List<ImageDto.Response> imageItemUrls;
		private List<String> styles;

		private LocalDateTime created;
		private LocalDateTime updated;

		public static ResponseForAuction toResponseForAuction(Item item) {
			List<ImageItem> imageItems = item.getImageItems();
			List<StyleItem> styleItems = item.getStyleItems();
			Category category = item.getCategory();
			return ResponseForAuction.builder()
				.id(item.getId())
				.itemNumber(
					item.getCreated().format(DateTimeFormatter.BASIC_ISO_DATE) + item.getId())
				.name(item.getName())
				.thumbnail(item.getThumbnail())
				.startPrice(item.getStartPrice())
				.width(item.getWidth())
				.depth(item.getDepth())
				.height(item.getHeight())
				.material(item.getMaterial())
				.conditionGrade(item.getConditionGrade())
				.conditionDescription(item.getConditionDescription())
				.text(item.getText())
				.designer(item.getDesigner())
				.brand(item.getBrand())
				.productionYear(item.getProductionYear())
				.created(item.getCreated())
				.updated(item.getUpdated())
				.writer(item.getMember().getNickName())
				.category(category == null ? null :
					CategoryDto.ResponseForItem.getResponseForItem(category, null))
				.imageItemUrls(imageItems == null ? new ArrayList<>() :
					imageItems.stream()
						.map(x -> ImageDto.Response.builder()
							.id(x.getId())
							.url(x.getUrl())
							.build())
						.collect(Collectors.toList()))
				.styles(styleItems == null ? new ArrayList<>() :
					styleItems.stream().map(StyleItem::getName)
						.collect(Collectors.toList()))
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class ListResponse {

		private Long id;

		private String name;

		private String thumbnail;

		private String writer;
		private String state;

		private Long startPrice;

		private LocalDateTime created;
		private LocalDateTime updated;

		@QueryProjection
		public ListResponse(
			Long id,
			String name,
			String thumbnail,
			String writer,
			State state,
			Long startPrice,
			LocalDateTime created,
			LocalDateTime updated) {
			this.id = id;
			this.name = name;
			this.thumbnail = thumbnail;
			this.writer = writer;
			this.state = state.getDescription();
			this.startPrice = startPrice;
			this.created = created;
			this.updated = updated;
		}
	}
}

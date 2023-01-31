package com.gokkan.gokkan.domain.expertComment.domain.dto;

import com.gokkan.gokkan.domain.expertComment.domain.ExpertComment;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExpertCommentDto {

	@Getter
	@Schema(name = "전문가 의견 생성 요청", description = "전문가 의견 생성 요청")
	public static class RequestCreateExpertComment {

		@NotNull(message = "itemId 은 null 일 수 없습니다.")
		private final Long itemId;
		@NotNull(message = "comment 은 null 일 수 없습니다.")
		private final String comment;
		@NotNull(message = "minPrice 은 null 일 수 없습니다.")
		private final Long minPrice;
		@NotNull(message = "maxPrice 은 null 일 수 없습니다.")
		private final Long maxPrice;
		@NotNull(message = "status 은 null 일 수 없습니다.")
		private final State status;

		@Builder
		public RequestCreateExpertComment(Long itemId, String comment, Long minPrice, Long maxPrice,
			State status) {
			this.itemId = itemId;
			this.comment = comment;
			this.minPrice = minPrice;
			this.maxPrice = maxPrice;
			this.status = status;
		}

		public static ExpertComment toEntity(RequestCreateExpertComment requestCreateExpertComment,
			ExpertInfo expertInfo, Item item) {
			return ExpertComment.builder()
				.expertInfo(expertInfo)
				.item(item)
				.comment(requestCreateExpertComment.getComment())
				.minPrice(requestCreateExpertComment.getMinPrice())
				.maxPrice(requestCreateExpertComment.getMaxPrice())
				.build();
		}
	}

	@Getter
	public static class ResponseExpertComment {

		private final String name;
		private final String profileImageUrl;
		private final String comment;
		private final Long minPrice;
		private final Long maxPrice;
		private String styles;

		@Builder
		public ResponseExpertComment(String name, String profileImageUrl, String comment,
			Long minPrice, Long maxPrice) {
			this.name = name;
			this.profileImageUrl = profileImageUrl;
			this.comment = comment;
			this.minPrice = minPrice;
			this.maxPrice = maxPrice;
		}

		public void setStyles(List<ExpertStyle> expertStyles) {
			StringBuilder stringBuffer = new StringBuilder();
			for (int i = 0; i < expertStyles.size(); i++) {
				stringBuffer.append(expertStyles.get(i).getStyleName());
				if (i != expertStyles.size() - 1) {
					stringBuffer.append(", ");
				}
			}
			this.styles = stringBuffer.toString();
		}
	}
}

package com.gokkan.gokkan.domain.auction.domain.dto;

import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.domain.type.SortType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class AuctionDto {

	@Getter
	@Builder
	@Schema(name = "경매 정보", description = "경매 정보")
	public static class ResponseAuctionInfo {

		private LocalDateTime auctionEndDateTime;
		private Long currentPrice;
	}

	@Getter
	@Builder
	@Schema(name = "경매 히스토리", description = "경매 히스토리")
	public static class ResponseAuctionHistory {

		private String memberId;
		private Long price;
		private LocalDateTime bidTime;

		public static ResponseAuctionHistory of(String memberId, Long price,
			LocalDateTime bidTime) {
			return ResponseAuctionHistory.builder()
				.memberId(memberId)
				.price(price)
				.bidTime(bidTime)
				.build();
		}
	}


	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ToString
	public static class FilterListRequest {

		private String category;
		private List<String> styles;
		private Long minPrice;
		private Long maxPrice;
		private String memberNickName;
		private SortType sort;
		private AuctionStatus auctionStatus;
		private boolean bid;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ToString
	public static class SimilarListRequest {

		private String category;
		private Long auctionId;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@Builder
	@Schema(name = "경매 List filter api 주요 정보 response")
	public static class ListResponse {

		private Long id;
		private Long itemId;

		private String name;

		private String thumbnail;

		private Long currentPrice;

		private String writer;
		private String auctionState;

		private LocalDateTime auctionEndDateTime;

		@QueryProjection
		public ListResponse(
			Long id,
			Long itemId,
			String name,
			String thumbnail,
			Long currentPrice,
			String writer,
			AuctionStatus auctionState,
			LocalDateTime auctionEndDateTime
		) {
			this.id = id;
			this.itemId = itemId;
			this.name = name;
			this.currentPrice = currentPrice;
			this.thumbnail = thumbnail;
			this.writer = writer;
			this.auctionState = auctionState.getDescription();
			this.auctionEndDateTime = auctionEndDateTime;
		}

		@Builder
		public ListResponse(
			Long id,
			Long itemId,
			String name,
			String thumbnail,
			Long currentPrice,
			String writer
		) {
			this.id = id;
			this.itemId = itemId;
			this.name = name;
			this.currentPrice = currentPrice;
			this.thumbnail = thumbnail;
			this.writer = writer;
		}
	}

	@Getter
	@Builder
	@Schema(name = "주문 상세 (배송지)")
	public static class AuctionOrderDetailAddress {

		private String name;
		private String phoneNumber;
		private String address;
		private String addressDetail;
	}

	@Getter
	@Builder
	@Schema(name = "주문 상세 (주문 상품)")
	public static class AuctionOrderDetailItem {

		private Long id;
		private Long itemId;
		private String itemName;
		private String thumbnail;
		private Long price;
	}

	@Getter
	@Builder
	@Schema(name = "주문 상세 (결제 금액)")
	public static class AuctionOrderDetailPaymentAmount {

		private Long hammerPrice;
		private Long fee;
		private Long paymentAmount;
	}
}

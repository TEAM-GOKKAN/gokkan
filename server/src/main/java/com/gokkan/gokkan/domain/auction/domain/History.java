package com.gokkan.gokkan.domain.auction.domain;

import java.time.LocalDateTime;
import java.util.StringTokenizer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {

	private Long memberId;
	private Long price;
	private LocalDateTime bidTime;

	@Builder
	public History(Long memberId, Long price, LocalDateTime bidTime) {
		this.memberId = memberId;
		this.price = price;
		this.bidTime = bidTime;
	}

	public static History toHistory(String history) {
		StringTokenizer st = new StringTokenizer(history, "_");
		Long memberId = Long.parseLong(st.nextToken());
		Long price = Long.parseLong(st.nextToken());
		LocalDateTime bidTime = LocalDateTime.parse(st.nextToken());
		return History.builder()
			.memberId(memberId)
			.price(price)
			.bidTime(bidTime)
			.build();
	}

	public String toString() {
		return memberId + "_" + price + "_" + bidTime;
	}
}

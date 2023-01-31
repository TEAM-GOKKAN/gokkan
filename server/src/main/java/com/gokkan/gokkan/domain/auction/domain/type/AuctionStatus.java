package com.gokkan.gokkan.domain.auction.domain.type;

import com.gokkan.gokkan.domain.member.exception.AuthErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuctionStatus {
	STARTED("경매중"),
	ENDED("마감"),
	WAIT_PAYMENT("결제대기");

	private final String description;

	public static AuctionStatus getAuctionStatus(String description) {
		AuctionStatus[] values = AuctionStatus.values();
		for (AuctionStatus auctionStatus : values) {
			if (auctionStatus.getDescription().equals(description)) {
				return auctionStatus;
			}
		}
		throw new RestApiException(AuthErrorCode.MISMATCH_AUCTION_STATE_TYPE);
	}
}

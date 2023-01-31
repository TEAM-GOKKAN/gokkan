package com.gokkan.gokkan.domain.auction.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

	AUCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "경매를 찾을 수 없습니다."),
	AUCTION_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "경매가 이미 종료되었습니다."),
	AUCTION_PRICE_IS_LOWER_THAN_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "입찰가가 현재 입찰가보다 낮습니다."),
	AUCTION_ANOTHER_USER_IS_BIDDING(HttpStatus.BAD_REQUEST, "다른 사용자가 경매에 입찰중입니다."),
	AUCTION_FAILED_TO_GET_LOCK(HttpStatus.BAD_REQUEST, "경매에 입찰하는데 실패했습니다."),
	AUCTION_ALREADY_BID(HttpStatus.BAD_REQUEST, "이미 입찰한 경매입니다."),
	AUCTION_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "경매 내역을 찾을 수 없습니다."),
	AUCTION_PRICE_IS_LOWER_THAN_BID_INCREMENT(HttpStatus.BAD_REQUEST, "호가 단위 이하의 금액입니다."),
	AUCTION_STATUS_IS_NOT_WAIT_PAYMENT(HttpStatus.BAD_REQUEST, "결제 대기 중인 경매가 아닙니다.");

	private final HttpStatus httpStatus;
	private final String message;
}

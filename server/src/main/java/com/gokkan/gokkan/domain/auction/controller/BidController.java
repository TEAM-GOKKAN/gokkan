package com.gokkan.gokkan.domain.auction.controller;

import com.gokkan.gokkan.domain.auction.service.BidService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.webSocket.interceptor.StompChannelInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BidController {

	private final BidService bidService;
	private final StompChannelInterceptor stompChannelInterceptor;

	@MessageMapping("/{auctionId}")
	public void message(
		@DestinationVariable Long auctionId,
		Long price) {

		Member member = stompChannelInterceptor.getMember();
		bidService.bidding(member, auctionId, price);
	}

	@MessageMapping("/auto/{auctionId}")
	public void autoBidding(
		@DestinationVariable Long auctionId,
		Long price) {
		Member member = stompChannelInterceptor.getMember();
		bidService.registrationAutoBid(member, auctionId, price);
	}

}

package com.gokkan.gokkan.global.iamport.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.exception.AuctionErrorCode;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.iamport.dto.PaymentDto.PaymentVerifyResponse;
import com.gokkan.gokkan.global.iamport.exception.IamportErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class IamportService {

	private final AuctionRepository auctionRepository;
	@Value("${iamport.imp-key}")
	private String IMP_KEY;
	@Value("${iamport.imp-secret}")
	private String IMP_SECRET;

	public String getAccessToken() {
		log.info("아임포트 엑세스 토큰 발급");
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("imp_key", IMP_KEY);
		formData.add("imp_secret", IMP_SECRET);
		WebClient webClient = WebClient.create("https://api.iamport.kr/users/getToken");
		String block = webClient.post()
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(formData)
			.retrieve()
			.bodyToMono(String.class)
			.block();
		JSONParser jsonParser = new JSONParser();
		String accessToken = "";
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			accessToken = (String) searchResponse.get("access_token");
		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		if (accessToken == null) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		log.info("아임포트 엑세스 토큰 발급 완료");
		return accessToken;
	}

	public String getPhoneNumber(String imp_uid, String accessToken) {
		log.info("아임포트 통합인증 휴대폰 번호 조회");
		WebClient webClient = WebClient.create("https://api.iamport.kr/certifications/" + imp_uid);
		JSONParser jsonParser = new JSONParser();
		String block = "";
		try {
			block = webClient.get()
				.header("Authorization", accessToken)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_NOT_FOUND);
		}
		String phone = "";
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			phone = (String) searchResponse.get("phone");
		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		log.info("아임포트 통합인증 휴대폰 번호 조회 완료");
		return phone;
	}

	@Transactional
	public PaymentVerifyResponse paymentVerify(Long auctionId, String imp_uid,
		String accessToken) {
		log.info("아임포트 결제 검증");
		WebClient webClient = WebClient.create("https://api.iamport.kr/payments/" + imp_uid);
		JSONParser jsonParser = new JSONParser();
		String block = "";
		try {
			block = webClient.get()
				.header("Authorization", accessToken)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_NOT_FOUND);
		}
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(
				AuctionErrorCode.AUCTION_NOT_FOUND));
		String name;
		String phoneNumber;
		String address;
		String pay_method;
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			Long amount = (Long) searchResponse.get("amount");
			String status = (String) searchResponse.get("status");
			Long currentPrice = auction.getCurrentPrice();
			log.info("결제 상태 : " + status);
			log.info("결제 금액 : " + amount);
			log.info("경매 금액 : " + currentPrice + ", 수수료 : " + currentPrice / 10);
			log.info("필요 결제 금액 : " + (currentPrice + currentPrice / 10));
			name = (String) searchResponse.get("buyer_name");
			phoneNumber = (String) searchResponse.get("buyer_tel");
			address = (String) searchResponse.get("buyer_addr");
			pay_method = (String) searchResponse.get("pg_provider");
			log.info("결제 PG사 : " + pay_method);
			if (status.equals("failed")) {
				throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
			} else if (status.equals("ready")) {
				throw new RestApiException(IamportErrorCode.IAMPORT_PAYMENT_STATUS_IS_READY);
			} else if (!amount.equals(currentPrice + currentPrice / 10)) {
				paymentCancel(imp_uid, accessToken);
				throw new RestApiException(IamportErrorCode.IAMPORT_NOT_MATCH_AMOUNT);
			}
			//TODO: 결제 완료 처리
			//auction.setAuctionStatus(AuctionStatus.ENDED);
		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		auctionRepository.save(auction);
		log.info("아임포트 결제 검증 완료");
		log.info("주문자 명 : " + name);
		log.info("주문자 전화번호 : " + phoneNumber);
		log.info("배송지 : " + address);
		return PaymentVerifyResponse.builder()
			.name(name)
			.phoneNumber(phoneNumber)
			.address(address)
			.pay_method(pay_method)
			.build();
	}

	private void paymentCancel(String imp_uid, String accessToken) {
		log.info("아임포트 결제 취소");
		WebClient webClient = WebClient.create("https://api.iamport.kr/payments/cancel/" + imp_uid);
		JSONParser jsonParser = new JSONParser();
		String block = "";
		try {
			block = webClient.get()
				.header("Authorization", accessToken)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_NOT_FOUND);
		}
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			String status = (String) searchResponse.get("status");
			log.info("결제 취소 상태 : " + status);
			if (status.equals("failed") || status.equals("ready") || status.equals("paid")) {
				log.info("결제 취소 안됨");
				throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
			}

		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		log.info("아임포트 결제 취소 완료");
	}
}

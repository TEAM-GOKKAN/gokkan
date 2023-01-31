package com.gokkan.gokkan.domain.member.domain.dto;

import com.gokkan.gokkan.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

	@Getter
	@Schema(name = "유저 정보 수정 요청")
	public static class RequestUpdateDto {

		private String name;
		private String nickName;
		private String phoneNumber;
		private String profileImageUrl;
		private String address;
		private String addressDetail;
		private String cardNumber;
	}

	@Getter
	@Schema(name = "유저 정보")
	public static class ResponseDto {

		private final String name;
		private final String nickName;
		private final String phoneNumber;
		private final String profileImageUrl;
		private final String address;
		private final String addressDetail;
		private final String cardNumber;

		@Builder
		public ResponseDto(String name, String nickName, String phoneNumber, String profileImageUrl,
			String address, String addressDetail, String cardNumber) {
			this.name = name;
			this.nickName = nickName;
			this.phoneNumber = phoneNumber;
			this.profileImageUrl = profileImageUrl;
			this.address = address;
			this.addressDetail = addressDetail;
			this.cardNumber = cardNumber;
		}

		public static ResponseDto fromEntity(Member member) {
			return ResponseDto.builder()
				.name(member.getName())
				.nickName(member.getNickName())
				.phoneNumber(member.getPhoneNumber())
				.profileImageUrl(member.getProfileImageUrl())
				.address(member.getAddress())
				.addressDetail(member.getAddressDetail())
				.cardNumber(member.getCardNumber())
				.build();
		}
	}

	@Getter
	@Schema(name = "판매자 정보")
	@Builder
	public static class ResponseSellerInfo {

		private final String name;
		private final String profileImageUrl;
		private final LocalDateTime createdAt;
	}

}

package com.gokkan.gokkan.domain.expertInfo.domain.dto;


import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class ExpertInfoDto {

	@Getter
	@Schema(name = "전문가 정보 생성 요청(멤버 아이디) DTO")
	public static class RequestCreateExpertInfoByMemberId {

		@NotNull(message = "memberId 는 null 일 수 없습니다.")
		private Long memberId;
		@NotNull(message = "info 는 null 일 수 없습니다.")
		private String info;

		@Builder
		public RequestCreateExpertInfoByMemberId(Long memberId, String info) {
			this.memberId = memberId;
			this.info = info;
		}
	}

	@Getter
	@Schema(name = "전문가 정보 생성 요청(멤버 닉네임) DTO")
	public static class RequestCreateExpertInfoByNickName {

		@NotNull(message = "nickName 는 null 일 수 없습니다.")
		private String nickName;
		@NotNull(message = "info 는 null 일 수 없습니다.")
		private String info;

		@Builder
		public RequestCreateExpertInfoByNickName(String nickName, String info) {
			this.nickName = nickName;
			this.info = info;
		}
	}

	@Getter
	@Schema(name = "전문가 정보 조회 결과 DTO")
	public static class ResponseGetExpertInfo {

		private String name;
		private String info;

		@Builder
		public ResponseGetExpertInfo(String name, String info) {
			this.name = name;
			this.info = info;
		}

		public static ResponseGetExpertInfo fromEntity(ExpertInfo expertInfo) {
			return new ResponseGetExpertInfo(expertInfo.getName(), expertInfo.getInfo());
		}
	}

}

package com.example.ddo_pay.user.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KakaoUserResponse {
	private Long id;

	private String connectedAt;

	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	private Properties properties;

	@Data
	public static class KakaoAccount {
		private Profile profile;
		private String name;
		private String email;
		private String phoneNumber;
		private String birthday;   // "MMDD"
		private String birthyear;  // "YYYY"
		// ...성별, 연령대 등이 필요하다면 필드 추가 가능
	}

	@Data
	public static class Profile {
		private String nickname;
		private String profileImageUrl;
		private String thumbnailImageUrl;
	}

	@Data
	public static class Properties {
		// Kakao API v2에서는 프로필 데이터가 kakao_account 아래 profile 필드로 많이 옮겨갔습니다.
		// 추가 custom 프로퍼티가 있을 경우 동적으로 매핑 가능하도록 예시
		@JsonAnySetter
		private Map<String, Object> additionalProperties;
	}
}
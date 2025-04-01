package com.example.ddo_pay.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfo {

	// 카카오에서 반환하는 사용자 고유 ID
	private Long id;

	// JSON의 "kakao_account" 객체를 매핑할 필드
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Getter
	@Setter
	public static class KakaoAccount {
		private String email;
		private Profile profile;

		@Getter
		@Setter
		public static class Profile {
			private String nickname;

			@JsonProperty("profile_image_url")
			private String profileImageUrl;

			@JsonProperty("thumbnail_image_url")
			private String thumbnailImageUrl;
			// 필요한 필드가 더 있다면 추가
		}
	}
}

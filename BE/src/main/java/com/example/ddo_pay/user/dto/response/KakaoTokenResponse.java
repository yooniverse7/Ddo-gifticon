package com.example.ddo_pay.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenResponse {

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private Long expiresIn;

	@JsonProperty("refresh_token_expires_in")
	private Long refreshTokenExpiresIn;

	private String scope;  // @JsonProperty 생략 가능 (필드명 동일 시)
}
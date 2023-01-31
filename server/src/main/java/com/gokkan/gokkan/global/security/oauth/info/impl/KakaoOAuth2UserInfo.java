package com.gokkan.gokkan.global.security.oauth.info.impl;

import com.gokkan.gokkan.global.security.oauth.info.OAuth2UserInfo;
import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getName() {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

		if (properties == null) {
			return null;
		}

		return (String) properties.get("nickname");
	}

	@Override
	public String getEmail() {
		return (String) getKakaoAccount().get("email");
	}

	public Map<String, Object> getKakaoAccount() {
		return (Map<String, Object>) attributes.get("kakao_account");
	}

	@Override
	public String getImageUrl() {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

		if (properties == null) {
			return null;
		}

		return (String) properties.get("thumbnail_image");
	}
}


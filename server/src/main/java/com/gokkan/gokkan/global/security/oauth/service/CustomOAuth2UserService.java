package com.gokkan.gokkan.global.security.oauth.service;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import com.gokkan.gokkan.global.security.oauth.entity.UserPrincipal;
import com.gokkan.gokkan.global.security.oauth.exception.OAuthProviderMissMatchException;
import com.gokkan.gokkan.global.security.oauth.info.OAuth2UserInfo;
import com.gokkan.gokkan.global.security.oauth.info.OAuth2UserInfoFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);

		try {
			return this.process(userRequest, user);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
		ProviderType providerType = ProviderType.valueOf(
			userRequest.getClientRegistration().getRegistrationId().toUpperCase());

		OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType,
			user.getAttributes());
		Member savedMember = memberRepository.findByUserId(userInfo.getId());

		if (savedMember != null) {
			if (providerType != savedMember.getProviderType()) {
				throw new OAuthProviderMissMatchException(
					"Looks like you're signed up with " + providerType +
						" account. Please use your " + savedMember.getProviderType()
						+ " account to login."
				);
			}
			updateUser(savedMember, userInfo);
		} else {
			savedMember = createUser(userInfo, providerType);
		}

		return new UserPrincipal(savedMember, user.getAttributes());
	}

	private Member createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
		LocalDateTime now = LocalDateTime.now();
		Member member = new Member(
			userInfo.getId(),
			userInfo.getName(),
			userInfo.getEmail(),
			userInfo.getImageUrl(),
			providerType,
			Role.USER,
			LocalDateTime.now());

		return memberRepository.saveAndFlush(member);
	}

	private Member updateUser(Member member, OAuth2UserInfo userInfo) {
		if (userInfo.getName() != null && !member.getName().equals(userInfo.getName())) {
			member.setName(userInfo.getName());
		}

		if (userInfo.getImageUrl() != null && !member.getProfileImageUrl()
			.equals(userInfo.getImageUrl())) {
			member.setProfileImageUrl(userInfo.getImageUrl());
		}

		return member;
	}
}


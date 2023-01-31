package com.gokkan.gokkan.global.security.oauth.entity;

import com.gokkan.gokkan.domain.member.domain.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails, OidcUser {

	private final String userId;
	private final ProviderType providerType;
	private final Role role;
	private final Collection<GrantedAuthority> authorities;
	private Map<String, Object> attributes;

	public UserPrincipal(Member member) {
		this.userId = member.getUserId();
		this.providerType = member.getProviderType();
		this.role = Role.USER;
		this.authorities = Collections.singletonList(
			new SimpleGrantedAuthority(Role.USER.getCode()));
	}

	public UserPrincipal(Member member, Map<String, Object> attributes) {
		this.userId = member.getUserId();
		this.providerType = member.getProviderType();
		this.role = Role.USER;
		this.authorities = Collections.singletonList(
			new SimpleGrantedAuthority(Role.USER.getCode()));
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getName() {
		return userId;
	}

	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getClaims() {
		return null;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return null;
	}

	@Override
	public OidcIdToken getIdToken() {
		return null;
	}
}


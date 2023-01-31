package com.gokkan.gokkan.domain.member.domain;

import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberAdapter extends User {

	private final Member member;

	public MemberAdapter(Member member) {
		super(member.getUserId(), "",
			Collections.singleton(new SimpleGrantedAuthority(Role.USER.getCode())));
		this.member = member;
	}
}

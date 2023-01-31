package com.gokkan.gokkan.global.security.oauth.service;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.MemberAdapter;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUserId(username);
		if (member == null) {
			throw new UsernameNotFoundException("Can not find username.");
		}
		return new MemberAdapter(member);
	}
}


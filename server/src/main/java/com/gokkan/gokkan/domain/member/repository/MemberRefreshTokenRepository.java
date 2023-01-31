package com.gokkan.gokkan.domain.member.repository;

import com.gokkan.gokkan.domain.member.domain.MemberRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

	MemberRefreshToken findByUserId(String userId);

	Optional<MemberRefreshToken> findByRefreshToken(String refreshToken);

	void deleteByUserId(String userId);
}


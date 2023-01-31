package com.gokkan.gokkan.domain.member.domain;

import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NotNull
	private String userId;

	private String name;
	@Column(unique = true)
	private String nickName;

	@Column(unique = true)
	@NotNull
	private String email;

	private String profileImageUrl;

	private String phoneNumber;
	private String cardNumber;
	private String address;
	private String addressDetail;

	@Enumerated(EnumType.STRING)
	@NotNull
	private ProviderType providerType;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;

	private LocalDateTime createdAt;

	@Builder
	public Member(
		@NotNull String userId,
		@NotNull String name,
		@NotNull String email,
		@NotNull String profileImageUrl,
		@NotNull ProviderType providerType,
		@NotNull Role role,
		@NotNull LocalDateTime createdAt) {

		this.userId = userId;
		this.name = name;
		this.email = email != null ? email : "NO_EMAIL";
		this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		this.providerType = providerType;
		this.role = role;
		this.createdAt = createdAt;
	}

}


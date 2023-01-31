package com.gokkan.gokkan.domain.auction.domain;

import com.gokkan.gokkan.domain.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AutoBidding {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auto_bidding_id")
	private Long id;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "auction_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Auction auction;

	private Long price;

	private LocalDateTime createdDate;

	@Builder
	public AutoBidding(Member member, Auction auction, Long price) {
		this.member = member;
		this.auction = auction;
		this.price = price;
		this.createdDate = LocalDateTime.now();
	}

	public void setPrice(Long price) {
		this.price = price;
	}
}

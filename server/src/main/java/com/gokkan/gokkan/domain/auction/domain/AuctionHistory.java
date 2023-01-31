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
public class AuctionHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auction_history_id")
	private Long Id;

	@JoinColumn(name = "auction_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Auction auction;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	private Long price;
	private LocalDateTime bidDateTime;

	@Builder
	public AuctionHistory(Auction auction, Member member, Long price, LocalDateTime bidDateTime) {
		this.auction = auction;
		this.member = member;
		this.price = price;
		this.bidDateTime = bidDateTime;
	}
}

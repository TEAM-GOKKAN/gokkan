package com.gokkan.gokkan.domain.auction.domain;

import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.expertComment.domain.ExpertComment;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auction_id")
	private Long id;

	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;

	private Long startPrice;
	private Long currentPrice;

	@Enumerated(value = EnumType.STRING)
	private AuctionStatus auctionStatus;

	@JoinColumn(name = "expert_comment_id")
	@OneToOne(fetch = FetchType.LAZY)
	private ExpertComment expertComment;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "item_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Item item;


	@Builder
	public Auction(LocalDateTime startDateTime, LocalDateTime endDateTime, Long startPrice,
		Long currentPrice,
		AuctionStatus auctionStatus, ExpertComment expertComment, Member member, Item item) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.startPrice = startPrice;
		this.currentPrice = currentPrice;
		this.auctionStatus = auctionStatus;
		this.expertComment = expertComment;
		this.member = member;
		this.item = item;
	}

	public void setCurrentPrice(Long price) {
		this.currentPrice = price;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void setAuctionStatus(AuctionStatus auctionStatus) {
		this.auctionStatus = auctionStatus;
	}
}

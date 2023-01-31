package com.gokkan.gokkan.domain.auction.repository;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.AutoBidding;
import com.gokkan.gokkan.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoBiddingRepository extends JpaRepository<AutoBidding, Long> {

	AutoBidding findByAuctionAndMember(Auction auction, Member member);

	AutoBidding findFirstByAuctionAndMemberNotAndPriceGreaterThanEqualOrderByCreatedDateAsc(
		Auction auction, Member member, Long price);
}


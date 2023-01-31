package com.gokkan.gokkan.domain.auction.repository;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionRepositoryCustom {

	List<Auction> findAllByEndDateTimeLessThanEqualAndAuctionStatusEquals(
		LocalDateTime currentDateTime, AuctionStatus auctionStatus);
}

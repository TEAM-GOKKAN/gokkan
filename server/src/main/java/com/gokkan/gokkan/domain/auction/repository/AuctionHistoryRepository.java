package com.gokkan.gokkan.domain.auction.repository;

import com.gokkan.gokkan.domain.auction.domain.AuctionHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, Long> {


	List<AuctionHistory> findAllByAuctionIdOrderByBidDateTimeDesc(Long auctionId);
}

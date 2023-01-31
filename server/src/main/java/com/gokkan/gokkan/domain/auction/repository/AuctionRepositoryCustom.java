package com.gokkan.gokkan.domain.auction.repository;

import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;
import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepositoryCustom {

	Page<ListResponse> searchAllFilter(FilterListRequest filterListRequest, Pageable pageable);

//	Page<ListResponse> searchMyBidAuction(String nickName, String auctionStatus, Pageable pageable);

	List<ListResponse> searchAllSimilar(SimilarListRequest similarListRequest);
}

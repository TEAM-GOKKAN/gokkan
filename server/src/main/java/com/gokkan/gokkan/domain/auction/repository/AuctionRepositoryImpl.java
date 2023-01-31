package com.gokkan.gokkan.domain.auction.repository;

import static com.gokkan.gokkan.domain.auction.domain.QAuction.auction;
import static com.gokkan.gokkan.domain.category.domain.QCategory.category;
import static com.gokkan.gokkan.domain.item.domain.QItem.item;
import static com.gokkan.gokkan.domain.style.domain.QStyleItem.styleItem;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.QAuctionDto_ListResponse;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.domain.type.SortType;
import com.gokkan.gokkan.domain.category.domain.Category;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ListResponse> searchAllFilter(FilterListRequest filterListRequest,
		Pageable pageable) {
		//TODO
		// 2. auction table index (auction_state, member_id, current_price, end_date_time)
		// 		auction_state -> range scan
		// 		member-id -> range scan
		// 		current-price -> range scan
		// 		endDateTime -> for order by
		// 3. item table index(category_id, member_id)
		// 		category_id -> range scan
		// 		member_id -> range scan
		List<ListResponse> content = jpaQueryFactory
			.select(new QAuctionDto_ListResponse(
					auction.id,
					item.id,
					item.name,
					item.thumbnail,
					auction.currentPrice,
					item.member.nickName,
					auction.auctionStatus,
					auction.endDateTime
				)
			).from(auction)
			.innerJoin(auction.item, item)
			.innerJoin(item.styleItems, styleItem)
			.where(
				eqAuctionStatus(filterListRequest.getAuctionStatus()),
				eqMemberNickNameForBid(
					filterListRequest.getMemberNickName(), filterListRequest.isBid()),
				minMaxPrice(filterListRequest.getMinPrice(), filterListRequest.getMaxPrice()),
				endTimeBeforeOrAfter(filterListRequest.getAuctionStatus()),
				eqCategory(filterListRequest.getCategory()),
				eqMemberNickName(filterListRequest.getMemberNickName(), filterListRequest.isBid()),
				eqStyle(filterListRequest.getStyles())
			)
			.groupBy(auction)
			.orderBy(
				filterListRequest.getSort().equals(SortType.DESC) ?
					auction.endDateTime.desc() : auction.endDateTime.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(auction.countDistinct()).from(auction)
			.innerJoin(auction.item, item)
			.innerJoin(item.styleItems, styleItem)
			.where(
				eqAuctionStatus(filterListRequest.getAuctionStatus()),
				eqMemberNickNameForBid(
					filterListRequest.getMemberNickName(), filterListRequest.isBid()),
				minMaxPrice(filterListRequest.getMinPrice(), filterListRequest.getMaxPrice()),
				endTimeBeforeOrAfter(filterListRequest.getAuctionStatus()),
				eqCategory(filterListRequest.getCategory()),
				eqMemberNickName(filterListRequest.getMemberNickName(), filterListRequest.isBid()),
				eqStyle(filterListRequest.getStyles())
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public List<ListResponse> searchAllSimilar(SimilarListRequest similarListRequest) {
		return jpaQueryFactory
			.select(new QAuctionDto_ListResponse(
					auction.id,
					item.id,
					item.name,
					item.thumbnail,
					auction.currentPrice,
					item.member.nickName,
					auction.auctionStatus,
					auction.endDateTime
				)
			).from(auction)
			.innerJoin(auction.item, item)
			.where(
				eqAuctionStatus(AuctionStatus.STARTED),
				eqCategory(similarListRequest.getCategory()),
				auction.endDateTime.after(LocalDateTime.now()),
				auction.id.eq(similarListRequest.getAuctionId()).not()
			)
			.groupBy(auction)
			.orderBy(auction.endDateTime.desc())
			.limit(5)
			.fetch();
	}

//	@Override
//	public Page<ListResponse> searchMyBidAuction(String nickName, String auctionStatus,
//		Pageable pageable) {
//		List<ListResponse> content = jpaQueryFactory
//			.select(new QAuctionDto_ListResponse(
//					auction.id,
//					item.id,
//					item.name,
//					item.thumbnail,
//					auction.currentPrice,
//					item.member.nickName,
//					auction.auctionStatus,
//					auction.endDateTime
//				)
//			).from(auction)
//			.innerJoin(auction.item, item)
//			.where(
//				eqMemberNickName(nickName, true),
//				eqAuctionStatus(auctionStatus)
//			)
//			.offset(pageable.getOffset())
//			.limit(pageable.getPageSize())
//			.fetch();
//
//		JPAQuery<Long> countQuery = jpaQueryFactory
//			.select(auction.count())
//			.from(auction)
//			.innerJoin(auction.item, item)
//			.where(
//				eqMemberNickName(nickName, true),
//				auction.auctionStatus.eq(WAIT_PAYMENT)
//			);
//
//		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
//	}

	public BooleanBuilder endTimeBeforeOrAfter(AuctionStatus auctionStatus) {
		if (auctionStatus.equals(AuctionStatus.STARTED)) {
			return new BooleanBuilder().and(auction.endDateTime.after(LocalDateTime.now()));
		} else {
			return new BooleanBuilder().and(auction.endDateTime.before(LocalDateTime.now()));
		}
	}

	private BooleanBuilder eqCategory(String categoryName) {
		if (categoryName == null) {
			return null;
		}
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		List<String> children = getCategoryChildren(categoryName);

		for (String c : children) {
			booleanBuilder.or(item.category.name.eq(c));
		}

		return booleanBuilder;
	}

	private List<String> getCategoryChildren(String categoryName) {
		Category category1 = jpaQueryFactory.selectFrom(category)
			.where(category.name.eq(categoryName))
			.fetchOne();
		List<String> children = new ArrayList<>();
		children.add(categoryName);
		if (category1 != null) {
			for (Category c : category1.getChildren()) {
				children.addAll(getCategoryChildren(c.getName()));
			}
		}

		return children;
	}


	private BooleanBuilder eqStyle(List<String> names) {

		if (names == null || names.size() == 0) {
			return null;
		}

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		for (String name : names) {
			booleanBuilder.or(styleItem.name.eq(name));
		}

		return booleanBuilder;
	}

	private BooleanBuilder eqMemberNickName(String memberNickName, boolean bidMember) {
		if (memberNickName == null) {
			return null;
		}
		if (!bidMember) {
			return new BooleanBuilder().or(item.member.nickName.eq(memberNickName));
		}

		return null;
	}

	private BooleanBuilder eqMemberNickNameForBid(String memberNickName, boolean bidMember) {
		if (memberNickName == null) {
			return null;
		}
		if (bidMember) {
			return new BooleanBuilder().or(auction.member.nickName.eq(memberNickName));
		}

		return null;
	}

	private BooleanBuilder eqAuctionStatus(AuctionStatus auctionStatus) {
		if (auctionStatus == null) {
			return null;
		}
		return new BooleanBuilder().or(
			auction.auctionStatus.eq(auctionStatus));
	}


	private BooleanBuilder minMaxPrice(Long minPrice, Long maxPrice) {
		if (maxPrice == null && minPrice == null) {
			return null;
		}
		return new BooleanBuilder()
			.or(auction.currentPrice.between(
					Objects.requireNonNullElse(minPrice, -1),
					Objects.requireNonNullElse(maxPrice, Long.MAX_VALUE)
				)
			);
	}
}
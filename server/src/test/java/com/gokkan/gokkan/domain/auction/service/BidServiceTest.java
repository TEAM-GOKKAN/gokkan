package com.gokkan.gokkan.domain.auction.service;

//@SpringBootTest
//class BidServiceTest {
//
//	@Autowired
//	private AuctionRepository auctionRepository;
//	@Autowired
//	private MemberRepository memberRepository;
//	@Autowired
//	private BidService bidService;
//
//
//	@Test
//	void test() {
//		//given
//		Auction auction = auctionRepository.save(Auction.builder()
//			.startDateTime(LocalDateTime.now())
//			.endDateTime(LocalDateTime.now().plusDays(7))
//			.startPrice(1000L)
//			.currentPrice(1000L)
//			.auctionStatus(AuctionStatus.STARTED)
//			.build());
//		Long auctionId = auction.getId();
//		Member member = memberRepository.save(Member.builder()
//			.name("name")
//				.userId("userId")
//				.role(Role.USER)
//				.providerType(ProviderType.KAKAO)
//				.email("email")
//				.profileImageUrl("profileImageUrl")
//			.build());
//
//		//when
//		bidService.bidding(member, auctionId, 1100L);
//
//		//then
//	}
//
//}
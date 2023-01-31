import React from 'react';
import LotListPage from '../components/common/LotListPage';

const MyBidLotPage = () => {
  const url = 'api/v1/auction/list/bid?auctionStatus=경매중&';

  return <LotListPage url={url} queryKey="myBidLot" title="입찰한 경매 목록" />;
};

export default MyBidLotPage;

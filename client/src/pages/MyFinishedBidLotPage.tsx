import React from 'react';
import LotListPage from '../components/common/LotListPage';

const MyFinishedBidLotPage = () => {
  const url = 'api/v1/auction/list/bid?auctionStatus=마감&';

  return (
    <LotListPage
      url={url}
      queryKey="myFinishedBidLot"
      title="결제완료된 경매 목록"
    />
  );
};

export default MyFinishedBidLotPage;

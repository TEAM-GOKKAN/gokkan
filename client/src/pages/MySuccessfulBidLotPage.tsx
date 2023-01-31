import SuccessfulLotListPage from '../components/MySuccessfulBidLot/SuccessfulLotListPage';

const MySuccessfulBidLotPage = () => {
  const url = 'api/v1/auction/list/bid?auctionStatus=결제대기&';

  return (
    <SuccessfulLotListPage
      url={url}
      queryKey="mySuccessfulBidLot"
      title="낙찰된 경매 목록"
    />
  );
};

export default MySuccessfulBidLotPage;

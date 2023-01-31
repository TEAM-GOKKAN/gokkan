import { Client, IMessage } from '@stomp/stompjs';
import { useAtom, useAtomValue } from 'jotai';
import { useUpdateAtom } from 'jotai/utils';
import React, { useCallback, useEffect, useRef } from 'react';
import useStomp from '../../lib/hooks/useStomp';
import {
  addedBidTimeAtom,
  auctionInfoAtom,
  bidErrMsgAtom,
  bidHistoryAtom,
  isTimeAddedAtom,
  StompClientAtom,
} from '../../store/bidAtom';
import {
  expertValuationAtom,
  lotDetailAtom,
  sellerInfoAtom,
} from '../../store/lotDetailAtom';
import BidBox from './BidBox';
import BidHistory from './BidHistory';
import CategoryInfo from './CategoryInfo';
import ExpertValuation from './ExpertValuation';
import Favorite from './Favorite';
import InfoDetail from './InfoDetail';
import InfoMain from './InfoMain';
import LotDescription from './LotDescription';
import SellerInfo from './SellerInfo';

interface IProps {
  auctionId: number;
}

export default function LotDetail({ auctionId }: IProps) {
  const lotDetail = useAtomValue(lotDetailAtom);
  const expertValuation = useAtomValue(expertValuationAtom);
  const sellerInfo = useAtomValue(sellerInfoAtom);
  const { data: auctionInfo, refetch: updateAuctionInfo } =
    useAtomValue(auctionInfoAtom);
  const { data: bidHistory, refetch: updateBidHistory } =
    useAtomValue(bidHistoryAtom);

  const [isTimeAdded, setIsTimeAdded] = useAtom(isTimeAddedAtom);

  // 웹소켓 Client
  const client = useRef<Client>();
  const setStompClient = useUpdateAtom(StompClientAtom);

  const [bidErrMsg, setBidErrMsg] = useAtom(bidErrMsgAtom);
  const [bidCloseTime, setBidCloseTime] = useAtom(addedBidTimeAtom);

  // 웹소켓 subscribe 콜백 함수
  const subscribeAuctionInfo = useCallback((message: IMessage) => {
    updateAuctionInfo();
    updateBidHistory();
  }, []);

  // 추가된 시간 받는 함수
  const subsribeAddedTime = useCallback((messege: IMessage) => {
    const data = JSON.parse(messege?.body);
    setBidCloseTime(data?.endDateTime);
    setIsTimeAdded(true);
  }, []);

  // 에러 subscribe 콜백 함수
  const subscribeBidError = useCallback((message: IMessage) => {
    const data = JSON.parse(message?.body);
    setBidErrMsg(data?.message);
  }, []);

  // subscription 목록
  const subscriptionList = [
    {
      destination: `/topic/${auctionId}`,
      callback: subscribeAuctionInfo,
    },
    {
      destination: `/topic/endDateTime/${auctionId}`,
      callback: subsribeAddedTime,
    },
    {
      destination: '/user/queue/error',
      callback: subscribeBidError,
    },
  ];

  // 웹소켓 연결 Hook
  let [connect, disconnect] = useStomp(client, subscriptionList);

  useEffect(() => {
    if (!auctionInfo) return;

    setBidCloseTime(auctionInfo.auctionEndDateTime);
  }, [auctionInfo?.auctionEndDateTime]);

  // 웹소켓 연결 및 해제
  useEffect(() => {
    connect();
    setStompClient(client);

    return () => disconnect();
  }, []);

  useEffect(() => {
    console.log('bid err msg');
    console.log(bidErrMsg);
  }, [bidErrMsg]);

  return (
    <div>
      <CategoryInfo category={lotDetail.category} />
      <InfoMain
        lotName={lotDetail.name}
        lotNumber={lotDetail.itemNumber}
        lotImageUrls={lotDetail.imageItemUrls}
      />
      {/* <Favorite /> */}
      <ExpertValuation data={expertValuation} />
      <InfoDetail
        brand={lotDetail.brand}
        designer={lotDetail.designer}
        material={lotDetail.material}
        period={lotDetail.productionYear}
        size={`${lotDetail.width} x ${lotDetail.depth} x ${lotDetail.height} cm`}
        conditionGrade={lotDetail.conditionGrade}
        conditionDescription={lotDetail.conditionDescription}
      />
      <LotDescription content={lotDetail.text} />
      <BidHistory bidHistory={bidHistory || []} />
      <SellerInfo data={sellerInfo} />
      <BidBox
        currentPrice={auctionInfo?.currentPrice || 0}
        closeTime={bidCloseTime}
        isTimeAdded={isTimeAdded}
        hasBid={bidHistory?.length ? true : false}
      />
    </div>
  );
}

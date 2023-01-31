import { useAtom } from 'jotai';
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PaymentResult from '../components/PaymentResult';
import { auctionIdAtom } from '../store/bidAtom';
import { lotIdAtom } from '../store/lotDetailAtom';

export default function PaymentResultPage() {
  const params = useParams();

  const [auctionId, setAuctionId] = useAtom(auctionIdAtom);
  const [lotId, setLotId] = useAtom(lotIdAtom);

  useEffect(() => {
    const { auctionId, itemId } = params;
    setAuctionId(Number(auctionId));
    setLotId(Number(itemId));
  }, []);

  // 경매 id 초기화
  useEffect(() => {
    return () => {
      setAuctionId(0);
    };
  }, []);

  return (
    <>{!!auctionId && !!lotId && <PaymentResult auctionId={auctionId} />}</>
  );
}

import { useAtom } from 'jotai';
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { auctionIdAtom } from '../store/bidAtom';
import { lotIdAtom } from '../store/lotDetailAtom';
import Payment from '../components/Payment';

declare global {
  interface Window {
    IMP: any;
  }
}

export default function PaymentPage() {
  const params = useParams();

  const [auctionId, setAuctionId] = useAtom(auctionIdAtom);
  const [itemId, setItemId] = useAtom(lotIdAtom);

  useEffect(() => {
    setAuctionId(Number(params.auctionId));
    setItemId(Number(params.itemId));
  }, []);

  // 경매 id, 상품 id 초기화
  useEffect(() => {
    return () => {
      setAuctionId(0);
      setItemId(0);
    };
  }, []);

  return (
    <>
      {!!auctionId && !!itemId && (
        <Payment auctionId={auctionId} itemId={itemId} />
      )}
    </>
  );
}

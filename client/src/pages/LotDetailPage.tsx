import React, { useEffect } from 'react';
import { lotIdAtom } from '../store/lotDetailAtom';
import { auctionIdAtom } from '../store/bidAtom';
import { useAtom } from 'jotai';
import { useParams } from 'react-router-dom';
import LotDetail from '../components/LotDetail';

export default function LotDetailPage() {
  const params = useParams();

  const [auctionId, setAuctionId] = useAtom(auctionIdAtom);
  const [lotId, setLotId] = useAtom(lotIdAtom);

  // itemId, auctionId 불러옴
  useEffect(() => {
    const { itemId, auctionId } = params;
    setLotId(Number(itemId));
    setAuctionId(Number(auctionId));
  }, []);

  // 경매 id, 상품 id 초기화
  useEffect(() => {
    return () => {
      setAuctionId(0);
      setLotId(0);
    };
  }, []);

  return <>{!!auctionId && !!lotId && <LotDetail auctionId={auctionId} />}</>;
}

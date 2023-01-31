import { useAtomValue } from 'jotai';
import React, { useCallback, useEffect, useState } from 'react';
import { lotDetailAtom } from '../../../store/lotDetailAtom';
import {
  auctionInfoAtom,
  bidHistoryAtom,
  isTimeAddedAtom,
} from '../../../store/bidAtom';
import { insertCommas } from '../../../utils/handleCommas';
import ModalFull from '../../common/ModalFull';
import BidSection from './BidSection';
import LotPreview from './LotPreview';
import BidConfirmModal from './BidConfirmModal';
import { getRemainingTime } from '../../../utils/getDiffTime';
import BidHistory from '../BidHistory';

export default function BidModal() {
  const { name, thumbnail } = useAtomValue(lotDetailAtom);
  const { data: auctionInfo, refetch: updateAuctionInfo } =
    useAtomValue(auctionInfoAtom);
  const { data: bidHistory, refetch: updateBidHistory } =
    useAtomValue(bidHistoryAtom);

  const [bidPrice, setBidPrice] = useState(0);
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [remainingTime, setRemainingTime] = useState('');
  const [isAutoBid, setIsAutoBid] = useState(false);
  const [isAuctionClosed, setIsAuctionClosed] = useState(false);

  // 남은 시간 업데이트 함수
  const updateRemainingTime = (time: string) => {
    const diffTime = getRemainingTime(time);
    setRemainingTime(diffTime);
  };

  const handleOpenConfirmModal = useCallback(() => {
    setConfirmModalOpen(true);
  }, []);

  const handleCloseConfirmModal = useCallback(() => {
    setConfirmModalOpen(false);
  }, []);

  const handleSetBidPrice = (price: number) => {
    setBidPrice(price);
  };

  // 마운트 시 리페칭
  useEffect(() => {
    updateAuctionInfo();
    updateBidHistory();
  }, []);

  // 경매 남은 시간 타이머
  useEffect(() => {
    if (!auctionInfo) return;

    updateRemainingTime(auctionInfo.auctionEndDateTime);
    const timeoutId = setInterval(
      () => updateRemainingTime(auctionInfo.auctionEndDateTime),
      1000
    );

    // 경매 마감 시 타이머 해제 및 응찰 섹션 숨기기
    if (remainingTime === '마감') {
      setIsAuctionClosed(true);
      clearInterval(timeoutId);
    }

    // 타이머 해제
    return () => clearInterval(timeoutId);
  }, [auctionInfo?.auctionEndDateTime, remainingTime]);

  return (
    <ModalFull title={remainingTime}>
      <LotPreview
        lotName={name}
        thumbnail={thumbnail}
        currentPrice={auctionInfo?.currentPrice}
        closeTime={auctionInfo?.auctionEndDateTime || ''}
        hasBid={bidHistory?.length ? true : false}
      />
      {!isAuctionClosed && (
        <BidSection
          currentPrice={auctionInfo?.currentPrice}
          hasBid={bidHistory?.length ? true : false}
          onSetBidPrice={handleSetBidPrice}
          onConfirmOpen={handleOpenConfirmModal}
          onSetAutoBid={setIsAutoBid}
        />
      )}
      <BidHistory bidHistory={bidHistory || []} />
      {confirmModalOpen && (
        <BidConfirmModal
          bidPrice={bidPrice}
          isAutoBid={isAutoBid}
          onConfirmClose={handleCloseConfirmModal}
        />
      )}
    </ModalFull>
  );
}

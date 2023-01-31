import { useAtom, useAtomValue } from 'jotai';
import React, { useCallback, useEffect } from 'react';
import styled from 'styled-components';
import {
  auctionIdAtom,
  bidErrMsgAtom,
  StompClientAtom,
} from '../../../store/bidAtom';
import { insertCommas } from '../../../utils/handleCommas';
import Modal from '../../common/Modal';

interface Iprops {
  bidPrice: number;
  isAutoBid: boolean;
  onConfirmClose: () => void;
}

export default function BidConfirmModal({
  bidPrice,
  isAutoBid,
  onConfirmClose,
}: Iprops) {
  const client = useAtomValue(StompClientAtom);

  const auctionId = useAtomValue(auctionIdAtom);
  const destination = isAutoBid
    ? `/auction/auto/${auctionId}`
    : `/auction/${auctionId}`;
  const accessToken = localStorage.getItem('accessToken');

  const bidErrMsg = useAtomValue(bidErrMsgAtom);

  const handlePlaceBid = useCallback(async () => {
    client?.current?.publish({
      destination,
      body: JSON.stringify(bidPrice),
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    onConfirmClose();
  }, []);

  useEffect(() => {
    console.log(bidErrMsg);
  }, [bidErrMsg]);

  return (
    <Modal buttonText="응찰" onSubmit={handlePlaceBid} onClose={onConfirmClose}>
      <BidPrice>
        <div>{isAutoBid ? '자동응찰가' : '응찰가'}</div>
        <div>{insertCommas(Number(bidPrice))}</div>
      </BidPrice>
      <CautionMessage>
        <div>응찰 버튼을 누르시면 취소가 불가능합니다.</div>
        <div>동시 응찰할 경우, 서버시각 기준 우선순위가 부여됩니다.</div>
      </CautionMessage>
    </Modal>
  );
}

const BidPrice = styled.div`
  font-size: var(--font-small);
  display: flex;
  flex-direction: column;
  align-items: center;

  & > div:last-child {
    font-size: var(--font-2x-large);
    font-weight: 500;
    margin-top: 5px;
  }
`;

const CautionMessage = styled.div`
  font-size: var(--font-small);
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;

  & > div {
    &:first-child {
      color: var(--color-orange);
      margin-bottom: 6px;
    }

    &:last-child {
      color: var(--color-brown300);
    }
  }
`;

import React from 'react';
import styled from 'styled-components';
import { insertCommas } from '../../utils/handleCommas';
import SuccessfulLotListElement from './SuccessfulLotListElement';

const SuccessfullLotList = ({ lotList, lotListCount }: PropType) => {
  const listElementCount = insertCommas(Number(lotListCount));

  return (
    <Container>
      <div className="total-count">총 {listElementCount} 개</div>
      <LotListGridWrapper>
        {lotList.map((lotInfo) => {
          return (
            <SuccessfulLotListElement lotInfo={lotInfo} key={lotInfo.id} />
          );
        })}
      </LotListGridWrapper>
    </Container>
  );
};

type LotInfoType = {
  id: number;
  itemId: number;
  name: string;
  thumbnail: string;
  currentPrice: number;
  writer: string;
  auctionState: string;
  auctionEndDateTime: string;
};

type PropType = {
  lotList: LotInfoType[];
  lotListCount?: number;
};

export default SuccessfullLotList;

const Container = styled.div`
  .total-count {
    margin-bottom: 18px;
    font-size: 14px;
    font-weight: 500;
  }
`;

const LotListGridWrapper = styled.div`
  display: grid;
  grid-template-columns: auto auto;
  column-gap: 16px;
`;

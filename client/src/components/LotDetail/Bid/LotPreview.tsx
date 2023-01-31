import React from 'react';
import styled from 'styled-components';
import { convertDateFormat } from '../../../utils/convertDateFormat';
import { insertCommas } from '../../../utils/handleCommas';

interface Iprops {
  lotName: string;
  thumbnail: string | undefined;
  currentPrice: number | string | undefined;
  closeTime: string;
  hasBid: boolean;
}

export default function LotPreview({
  lotName,
  thumbnail,
  currentPrice,
  closeTime,
  hasBid,
}: Iprops) {
  const { month, day, hour, min } = convertDateFormat(closeTime);

  return (
    <Container>
      <ImageContainer>
        <img src={thumbnail || ''} alt="thumbnail" />
      </ImageContainer>
      <LotInfo>
        <LotTitle>{lotName}</LotTitle>
        <CurrentPrice>
          <div>{hasBid ? '현재가' : '시작가'}</div>
          <div>
            <span>{insertCommas(String(currentPrice))}</span>
            <span>원</span>
          </div>
        </CurrentPrice>
        <CloseTime>
          <div>마감시간</div>
          <div>{`${month}월 ${day}일 ${hour}시 ${min}분`}</div>
        </CloseTime>
      </LotInfo>
    </Container>
  );
}

const Container = styled.div`
  margin-bottom: 42px;
  display: flex;
`;

const ImageContainer = styled.div`
  width: calc((100% - 16px) / 2);
  height: calc((100vw - 48px) / 2);
  background: var(--color-brown100);
  /* margin-bottom: 32px; */

  & > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const LotInfo = styled.div`
  width: calc((100% - 16px) / 2);
  margin-left: 16px;
  white-space: pre-line;
  line-height: 1.4;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;

  & * {
    letter-spacing: normal;
  }
`;

const LotTitle = styled.div`
  width: 100%;
  font-size: var(--font-medium);
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
`;

const CurrentPrice = styled.div`
  /* margin-top: 24px; */
  position: absolute;
  bottom: 52px;
  font-size: var(--font-x-large);
  font-weight: 500;

  & > div:first-child {
    font-size: var(--font-micro);
    color: var(--color-brown300);
    letter-spacing: -0.0625em;
  }
`;

const CloseTime = styled.div`
  font-size: var(--font-regular);
  font-weight: 500;
  color: var(--color-brown400);

  & > div:first-child {
    font-size: var(--font-micro);
    color: var(--color-brown300);
    letter-spacing: -0.0625em;
    margin-bottom: 2px;
  }
`;

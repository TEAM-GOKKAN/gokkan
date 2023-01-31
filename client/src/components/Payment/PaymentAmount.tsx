import React from 'react';
import styled from 'styled-components';
import { insertCommas } from '../../utils/handleCommas';
import Section from './Section';

interface IProps {
  data: {
    hammerPrice: number;
    fee: number;
    paymentAmount: number;
  };
}

export default function PaymentAmount({ data }: IProps) {
  const { hammerPrice, fee, paymentAmount } = data;

  return (
    <Section title="결제금액" preview={insertCommas(paymentAmount) + '원'}>
      <PriceList>
        <Price>
          <span>낙찰가</span>
          <span>{insertCommas(hammerPrice) + '원'}</span>
        </Price>
        <Price>
          <span>수수료</span>
          <span>{insertCommas(fee) + '원'}</span>
        </Price>
      </PriceList>
      <TotalPrice>
        <span>총 결제금액</span>
        <span>{insertCommas(paymentAmount) + '원'}</span>
      </TotalPrice>
    </Section>
  );
}

const PriceList = styled.div`
  &::after {
    content: '';
    display: inline-block;
    margin-top: 21px;
    width: 100%;
    border-bottom: 1px solid var(--color-brown100);
  }
`;

const Price = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 12px;

  & > span:first-child {
    color: var(--color-brown300);
  }

  & > span:last-child {
    letter-spacing: normal;
  }
`;

const TotalPrice = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 21px;
  font-size: var(--font-regular);

  & > span:last-child {
    letter-spacing: normal;
    font-weight: var(--weight-semi-bold);
  }
`;

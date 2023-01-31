import React from 'react';
import styled from 'styled-components';

interface IProps {
  onClickPayment: () => void;
}

export default function PayBox({ onClickPayment }: IProps) {
  return (
    <Container>
      <PayButton type="button" className="active" onClick={onClickPayment}>
        결제하기
      </PayButton>
    </Container>
  );
}

const Container = styled.div`
  position: fixed;
  left: 0;
  bottom: 0;

  width: 100%;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const PayButton = styled.button`
  width: 100%;
  height: 100%;
  font-size: var(--font-regular);
  font-weight: var(--weight-semi-bold);
  color: var(--color-brown300);
  background: var(--color-brown200);

  &.active {
    color: var(--color-brown100);
    background: var(--color-brown500);
  }
`;

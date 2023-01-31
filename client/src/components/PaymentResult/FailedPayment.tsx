import React from 'react';
import ModalFull from '../common/ModalFull';
import styled from 'styled-components';
import { SlClose } from 'react-icons/sl';
import { useNavigate } from 'react-router-dom';

interface IProps {
  errMsg: string;
}

export default function FailedPayment({ errMsg }: IProps) {
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate('/payment');
  };

  return (
    <ModalFull title="결제 실패" onClickBtn={handleGoBack}>
      <PaymentResult>
        <StyledCheckIcon />
        <ResultMessage>다시 시도해주시기 바랍니다.</ResultMessage>
        <ErrorMessage>{errMsg}</ErrorMessage>
      </PaymentResult>
    </ModalFull>
  );
}

const PaymentResult = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  top: calc((100% - 60px) / 2);
  left: 50%;
  transform: translate(-50%, -50%);
  width: 70%;
`;

const StyledCheckIcon = styled(SlClose)`
  font-size: 42px;
  color: var(--color-orange);
`;

const ResultMessage = styled.div`
  font-size: var(--font-regular);
  margin-top: 20px;
  margin-bottom: 10px;
  font-weight: var(--weight-semi-bold);
`;

const ErrorMessage = styled.div`
  font-size: var(--font-small);
  color: var(--color-brown300);
  width: 100%;
  white-space: pre-line;
  line-height: calc(var(--font-small) * 1.3);
  text-align: center;
`;

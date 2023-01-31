import React from 'react';
import ModalFull from '../common/ModalFull';
import styled from 'styled-components';
import ShippingAddress from '../Payment/ShippingAddress';
import { useAtomValue } from 'jotai';
import {
  addressAtom,
  paymentAmountAtom,
  pgAtom,
  productInfoAtom,
} from '../../store/paymentAtom';
import ProductInfo from '../Payment/ProductInfo';
import PaymentAmount from '../Payment/PaymentAmount';
import OrderNumber from './OrderNumber';
import PaidMethod from './PaidMethod';
import { SlCheck } from 'react-icons/sl';
import { useNavigate } from 'react-router-dom';

interface IProps {
  orderNumber: string;
}

export default function CompletedPayment({ orderNumber }: IProps) {
  const shippingAddress = useAtomValue(addressAtom);
  const productInfo = useAtomValue(productInfoAtom);
  const paymentAmount = useAtomValue(paymentAmountAtom);
  const pg = useAtomValue(pgAtom);

  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate('/');
  };

  return (
    <ModalFull title="결제 완료" onClickBtn={handleGoBack}>
      <PaymentResult>
        <StyledCheckIcon />
        <ResultMessage>결제가 완료되었습니다.</ResultMessage>
      </PaymentResult>
      <OrderNumber orderNumber={orderNumber} />
      <ShippingAddress data={shippingAddress} isPaid />
      <ProductInfo data={productInfo} />
      <PaymentAmount data={paymentAmount} />
      <PaidMethod pg={pg} />
    </ModalFull>
  );
}

const PaymentResult = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const StyledCheckIcon = styled(SlCheck)`
  font-size: 42px;
  color: var(--color-purple);
`;

const ResultMessage = styled.div`
  font-size: var(--font-regular);
  margin-top: 16px;
  margin-bottom: 42px;
  font-weight: var(--weight-semi-bold);
`;

import { useAtom, useAtomValue } from 'jotai';
import React, { ChangeEvent, useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { USER_CODE } from '../../lib/constants/payment';
import {
  addressAtom,
  iamportDataAtom,
  mRedirectUrlAtom,
  payErrMsgAtom,
  paymentAmountAtom,
  pgAtom,
  productInfoAtom,
} from '../../store/paymentAtom';
import customAxios from '../../utils/customAxios';
import Modal from '../common/Modal';
import ModalFull from '../common/ModalFull';
import PayBox from './PayBox';
import PaymentAmount from './PaymentAmount';
import PaymentMethods from './PaymentMethods';
import PaymentTerms from './PaymentTerms';
import ProductInfo from './ProductInfo';
import ShippingAddress from './ShippingAddress';
import { AiOutlineWarning } from 'react-icons/ai';

interface IProps {
  auctionId: number;
  itemId: number;
}

export default function Payment({ auctionId, itemId }: IProps) {
  const navigate = useNavigate();

  const shippingAddress = useAtomValue(addressAtom);
  const productInfo = useAtomValue(productInfoAtom);
  const paymentAmount = useAtomValue(paymentAmountAtom);
  const iamportData = useAtomValue(iamportDataAtom);
  const [pg, setPg] = useAtom(pgAtom);
  const [mRedirectUrl, setMRedirectUrl] = useAtom(mRedirectUrlAtom);
  const [errMsg, setErrMsg] = useAtom(payErrMsgAtom);

  const [isTermChecked, setIsTermChecked] = useState(false);
  const [validationErrMsg, setValidationErrMsg] = useState('');

  const callback = async (res: any) => {
    const {
      success,
      error_msg,
      imp_uid,
      merchant_uid,
      pay_method,
      paid_amount,
      status,
    } = res;
    if (success) {
      const { data } = await customAxios.get('/api/v1/payment', {
        params: {
          auctionId,
          impUid: imp_uid,
        },
      });

      if (data?.name) {
        navigate(
          `result?imp_uid=${imp_uid}&merchant_uid=${merchant_uid}&imp_success=${success}`
        );
      }
    } else {
      navigate(
        `result?imp_uid=${imp_uid}&merchant_uid=${merchant_uid}&imp_success=${success}&error_msg=${error_msg}`
      );
    }
  };

  useEffect(() => {
    console.log(validationErrMsg);
  }, [validationErrMsg]);

  const handleClickPaymnet = () => {
    if (!pg) {
      setErrMsg('결제수단을 선택해주세요.');
      return;
    }

    if (!isTermChecked) {
      setErrMsg('이용약관에 동의해주세요.');
      return;
    }

    const { IMP } = window;
    IMP.init(USER_CODE);

    IMP.request_pay(iamportData, callback);
  };

  const selectPaymentMethod = (method: string) => {
    setPg(method);
  };

  const closePopUp = useCallback(() => {
    setErrMsg('');
  }, []);

  const checkTermCheckbox = useCallback((e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setIsTermChecked(true);
    } else {
      setIsTermChecked(false);
    }
  }, []);

  useEffect(() => {
    setMRedirectUrl(
      `http://gokkan.s3-website.ap-northeast-2.amazonaws.com/payment/${itemId}/${auctionId}/result`
    );
  }, []);

  useEffect(() => {
    console.log(iamportData);
  }, [iamportData]);

  return (
    <>
      <ModalFull title="결제">
        <ShippingAddress data={shippingAddress} />
        <ProductInfo data={productInfo} />
        <PaymentAmount data={paymentAmount} />
        <PaymentMethods pg={pg} onSetPg={selectPaymentMethod} />
        <PaymentTerms onChangeCheckbox={checkTermCheckbox} />
        <PayBox onClickPayment={handleClickPaymnet} />
      </ModalFull>
      {errMsg && (
        <Modal onClose={closePopUp}>
          <StyledWarningIcon />
          <div>{errMsg}</div>
        </Modal>
      )}
    </>
  );
}

const StyledWarningIcon = styled(AiOutlineWarning)`
  font-size: 42px;
  color: var(--color-orange);
  margin-bottom: 16px;
`;

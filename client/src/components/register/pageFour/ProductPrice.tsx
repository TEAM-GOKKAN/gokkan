import React, { useState } from 'react';
import { productStartPriceAtom } from '../../../store/registerAtom';
import { useAtom } from 'jotai';
import styled from 'styled-components';

const StartPriceWrapper = styled.div`
  .title {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
    margin-bottom: 10px;
  }
  .price-holder {
    display: flex;
    align-items: center;
    position: relative;
    input {
      text-align: right;
      height: 42px;
      width: 100%;
      padding: 10px 12px;
      padding-right: 30px;
      background-color: none;
      border: 1px solid var(--color-brown100);
      font-weight: 400;
      font-size: 15px;
      line-height: 22.5px;
      font-family: 'Poppins';
    }
    .scale {
      height: 42px;
      display: flex;
      align-items: center;
      color: var(--color-brown300);
      position: absolute;
      right: 10px;
    }
  }
  .warning {
    color: var(--color-orange);
    margin-top: 10px;
  }
`;

const ProductPrice = () => {
  const [startPrice, setStartPrice] = useAtom(productStartPriceAtom);
  const [checkPrice, setCheckPrice] = useState('');

  const handleStartValue = (e: React.ChangeEvent<HTMLInputElement>) => {
    let targetValue = e.target.value;
    targetValue = targetValue.replace(/[^0-9]/g, '');
    targetValue = Number(targetValue).toLocaleString();
    setStartPrice(targetValue);
  };

  const handleCheckPrice = () => {
    const checkNumber = Number(startPrice.replace(/,/g, ''));
    if (checkNumber >= 10000) {
      setCheckPrice('good');
    } else {
      setCheckPrice('bad');
    }
  };

  return (
    <StartPriceWrapper>
      <div className="title">시작가</div>
      <div className="price-holder">
        <input
          type="text"
          pattern="\d*"
          onChange={handleStartValue}
          onBlur={handleCheckPrice}
          value={startPrice}
        />
        <div className="scale">원</div>
      </div>
      {checkPrice === 'bad' && (
        <div className="warning">유효한 숫자를 입력해주세요</div>
      )}
    </StartPriceWrapper>
  );
};

export default ProductPrice;

import React from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import { minPriceFilterAtom, maxPriceFilterAtom } from '../../store/filterAtom';

const PriceFilter = () => {
  const [minPrice, setMinPrice] = useAtom(minPriceFilterAtom);
  const [maxPrice, setMaxPrice] = useAtom(maxPriceFilterAtom);

  const handleMinPriceInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    let targetValue = e.target.value;
    targetValue = targetValue.replace(/[^0-9]/g, '');
    targetValue = Number(targetValue).toLocaleString();
    setMinPrice(targetValue);
  };

  const handleMaxPriceInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    let targetValue = e.target.value;
    targetValue = targetValue.replace(/[^0-9]/g, '');
    targetValue = Number(targetValue).toLocaleString();
    setMaxPrice(targetValue);
  };

  return (
    <Container>
      <div className="title">가격</div>
      <div className="price-holder">
        <input
          type="text"
          pattern="\d*"
          onChange={handleMinPriceInput}
          value={minPrice}
          placeholder="0"
        />
        <div className="seperator">~</div>
        <input
          type="text"
          pattern="\d*"
          onChange={handleMaxPriceInput}
          value={maxPrice}
          placeholder="0"
        />
      </div>
    </Container>
  );
};

export default PriceFilter;

const Container = styled.div`
  margin-bottom: 48px;
  .title {
    font-weight: 700;
    font-size: 16px;
    margin-bottom: 20px;
  }
  .price-holder {
    width: 100%;
    height: 42px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    input {
      padding: 12px;
    }
    .seperator {
      margin: 0 10px;
    }
  }
`;

import React, { useRef } from 'react';
import styled from 'styled-components';
import { useAtom, PrimitiveAtom } from 'jotai';

interface ExaminePriceInputType {
  title: string;
  targetAtom: PrimitiveAtom<string>;
}

const ExaminePriceInput = ({ title, targetAtom }: ExaminePriceInputType) => {
  const [price, setPrice] = useAtom(targetAtom);
  const containerRef = useRef<HTMLDivElement>(null);

  const handleStartValue = (e: React.ChangeEvent<HTMLInputElement>) => {
    let targetValue = e.target.value;
    targetValue = targetValue.replace(/[^0-9]/g, '');
    targetValue = Number(targetValue).toLocaleString();
    setPrice(targetValue);
  };

  const handleInputFocus = () => {
    if (!containerRef.current) return;
    containerRef.current.scrollIntoView({ block: 'start', inline: 'nearest' });
  };

  return (
    <Container ref={containerRef}>
      <div className="title">{title}</div>
      <div className="price-holder">
        <input
          type="text"
          pattern="\d*"
          onChange={handleStartValue}
          onFocus={handleInputFocus}
          value={price}
          placeholder={'0'}
        />
        <div className="scale">원</div>
      </div>
    </Container>
  );
};

export default ExaminePriceInput;

const Container = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 42px;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  .price-holder {
    display: flex;
    align-items: center;
    border: 1px solid var(--color-brown300);
    input {
      text-align: right;
      width: 100%;
      border: none;
    }
    .scale {
      height: 42px;
      display: flex;
      align-items: center;
      color: var(--color-brown300);
      background-color: var(--color-brown100);
      padding-right: 11px;
    }
  }
`;

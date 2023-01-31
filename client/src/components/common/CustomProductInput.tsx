import React, { useEffect } from 'react';
import styled from 'styled-components';
import { PrimitiveAtom, useAtom } from 'jotai';

const ProductInputWrapper = styled.div`
  display: flex;
  flex-direction: column;
  label {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
    margin-bottom: 10px;
  }
  input {
    height: 42px;
    width: 100%;
    padding: 10px 12px;
    background-color: none;
    border: 1px solid var(--color-brown100);
    font-weight: 400;
    font-size: 15px;
    line-height: 22.5px;
    font-family: 'Noto Sans KR';
  }
`;

const CustomProductInput = ({
  title,
  storeAtom,
  placeHolder,
  inputType = 'text',
}: ProductInputProps) => {
  const [storeValue, setStoreValue] = useAtom(storeAtom);

  const handleTargetInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setStoreValue(e.target.value);
  };

  return (
    <ProductInputWrapper>
      <label htmlFor={title}>{title}</label>
      <input
        type={inputType}
        id={title}
        placeholder={placeHolder}
        value={storeValue}
        onChange={handleTargetInputChange}
      />
    </ProductInputWrapper>
  );
};

type ProductInputProps = {
  title: string;
  storeAtom: PrimitiveAtom<string> | PrimitiveAtom<number>;
  placeHolder: string;
  inputType?: string;
};

export default CustomProductInput;

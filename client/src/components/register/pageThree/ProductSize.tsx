import React from 'react';
import styled from 'styled-components';
import { AiOutlineClose } from 'react-icons/ai';
import { useAtom, SetStateAction } from 'jotai';
import {
  productWidthAtom,
  productHeightAtom,
  productDepthAtom,
} from '../../../store/registerAtom';

const ProductSizeWrapper = styled.div`
  margin-bottom: 40px;
  .title {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
    margin-bottom: 10px;
  }
  .actual-size {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 42px;
    input {
      width: 91px;
      padding: 10px;
      height: 42px;
      background: none;
      border: 1px solid var(--color-brown100);
      font-size: 15px;
      font-weight: 400;
      line-height: 21.7px;
      font-family: 'Poppins';
    }
  }
`;

const ProductSize = () => {
  const [productWidth, setProductWidth] = useAtom(productWidthAtom);
  const [productDepth, setProductDepth] = useAtom(productDepthAtom);
  const [productHeight, setProductHeight] = useAtom(productHeightAtom);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    setFunc: (update: SetStateAction<string>) => void
  ) => {
    setFunc(e.target.value);
  };

  return (
    <ProductSizeWrapper>
      <div className="title">사이즈</div>
      <div className="actual-size">
        <input
          type="number"
          placeholder="가로"
          onChange={(e) => {
            handleInputChange(e, setProductWidth);
          }}
          value={productWidth}
        />
        <AiOutlineClose />
        <input
          type="number"
          placeholder="세로"
          onChange={(e) => {
            handleInputChange(e, setProductDepth);
          }}
          value={productDepth}
        />
        <AiOutlineClose />
        <input
          type="number"
          placeholder="높이"
          onChange={(e) => {
            handleInputChange(e, setProductHeight);
          }}
          value={productHeight}
        />
        <div>cm</div>
      </div>
    </ProductSizeWrapper>
  );
};

export default ProductSize;

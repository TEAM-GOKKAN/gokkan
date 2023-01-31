import React from 'react';
import styled from 'styled-components';
import ProductListElement from './ProductListElement';
import { insertCommas } from '../../utils/handleCommas';

const ProductListWrapper = styled.div`
  .total-count {
    margin-bottom: 19px;
    font-size: 14px;
    font-weight: 500;
    line-height: 14px;
    letter-spacing: -4%;
  }
`;

const ProductListGridWrapper = styled.div`
  display: grid;
  grid-template-columns: auto auto;
  column-gap: 16px;
`;

const ProductList = ({
  productList,
  productListNumber,
  targetUrl,
}: ProductListPropType) => {
  const listElementCount = insertCommas(Number(productListNumber));

  return (
    <ProductListWrapper>
      <div className="total-count">총 {listElementCount} 개</div>
      <ProductListGridWrapper>
        {productList.map((productInfo) => {
          return (
            <ProductListElement
              productInfo={productInfo}
              targetUrl={targetUrl}
              key={productInfo.id}
            />
          );
        })}
      </ProductListGridWrapper>
    </ProductListWrapper>
  );
};

type ProductInfoType = {
  id: string;
  name: string;
  thumbnail: string;
  writer: string;
  created: string;
  updated: string;
  startPrice: number;
};

type ProductListPropType = {
  productList: ProductInfoType[];
  targetUrl: string;
  productListNumber?: number;
};

export default ProductList;

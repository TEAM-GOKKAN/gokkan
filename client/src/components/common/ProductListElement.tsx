import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { insertCommas } from '../../utils/handleCommas';

const ListElementContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: calc(50vw - 24px);
  margin-bottom: 48px;
  img {
    object-fit: cover;
  }
  img,
  .none-image {
    height: calc((50vw - 24px) * 1.5);
    width: 100%;
    margin-bottom: 12px;
    border: 0.5px solid var(--color-brown200);
    background-color: var(--color-brown200);
  }
  .element-title {
    margin-bottom: 12px;
    font-weight: 400;
    font-size: var(--font-regular);
    line-height: 17.5px;
    width: 100%;
    white-space: pre-line;
    .untitled {
      color: var(--color-brown300);
    }
  }
  .price {
    display: flex;
    flex-direction: column;
    color: var(--color-brown300);
    [data-price='true'] {
      color: var(--color-brown500);
      font-size: 16px;
      font-weight: 600;
      font-family: 'Poppins';
    }
    .price-title {
      font-weight: 500;
      font-size: 12px;
      line-height: 17px;
      letter-spacing: -4%;
      color: var(--color-brown300);
    }
    .price-content {
      margin-top: 4px;
      display: flex;
      flex-direction: row;
      .unit {
        margin-left: 4px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
`;

const ProductListElement = ({
  productInfo,
  targetUrl,
}: ProductListElementPropType) => {
  const [price, setPrice] = useState('-');
  const navigate = useNavigate();

  const handleElementClick = () => {
    navigate(`${targetUrl}/${productInfo.id}`);
  };

  useEffect(() => {
    if (productInfo.startPrice && productInfo.startPrice !== 0) {
      setPrice(insertCommas(productInfo.startPrice));
    }
  }, [productInfo.startPrice]);

  return (
    <ListElementContainer onClick={handleElementClick}>
      {productInfo.thumbnail ? (
        <img src={productInfo.thumbnail} />
      ) : (
        <div className="none-image" />
      )}
      <div className="element-title">
        {productInfo.name === '' ? (
          <p className="untitled">Untitled</p>
        ) : (
          productInfo.name
        )}
      </div>
      <div className="price">
        <p className="price-title">시작가</p>
        <div className="price-content" data-price={price !== '-'}>
          {price}
          <p className="unit">원</p>
        </div>
      </div>
    </ListElementContainer>
  );
};

type ProductListElementPropType = {
  productInfo: {
    id: string;
    name: string;
    thumbnail: string;
    writer: string;
    created: string;
    updated: string;
    startPrice: number;
  };
  targetUrl: string;
};

export default ProductListElement;

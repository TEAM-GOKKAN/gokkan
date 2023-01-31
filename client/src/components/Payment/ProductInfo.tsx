import React from 'react';
import styled from 'styled-components';
import { insertCommas } from '../../utils/handleCommas';
import Section from './Section';

interface IProps {
  data: {
    id: number;
    itemId: number;
    itemName: string;
    thumbnail: string;
    price: number;
  };
}

export default function ProductInfo({ data }: IProps) {
  const { itemName, thumbnail, price } = data;

  return (
    <Section title="주문상품" preview={itemName}>
      <Content>
        <ThumbnailContent>
          <img src={thumbnail || ''} alt="thumbnail" />
        </ThumbnailContent>
        <NameAndPrice>
          <ProductName>{itemName}</ProductName>
          <ProductPrice>
            <div>낙찰가</div>
            <div>{insertCommas(price) + '원'}</div>
          </ProductPrice>
        </NameAndPrice>
      </Content>
    </Section>
  );
}

const Content = styled.div`
  width: 100%;
  display: flex;
  gap: 16px;

  & > div {
    width: calc((100% - 16px) / 2);
  }
`;

const ThumbnailContent = styled.div`
  background-color: var(--color-brown100);
  width: 100%;
  max-height: 112px;

  & img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const NameAndPrice = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  white-space: pre-line;
`;

const ProductName = styled.div`
  letter-spacing: normal;
  line-height: calc(var(--font-small) * 1.4);
`;

const ProductPrice = styled.div`
  & > div:first-child {
    font-size: var(--font-micro);
    color: var(--color-brown300);
  }

  & > div:last-child {
    letter-spacing: normal;
    margin-top: 6px;
  }
`;

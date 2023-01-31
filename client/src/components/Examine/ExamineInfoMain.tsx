import React from 'react';
import styled from 'styled-components';
import ImageSlider from '../common/ImageSlider';

interface Iprops {
  itemName: string;
  itemNumber: string;
  itemImageUrls: ImageUrl[];
  itemImageCheckUrls: ImageUrl[];
}

interface ImageUrl {
  id: number;
  url: string;
}

export default function ExamineInfoMain(props: Iprops) {
  const { itemName, itemNumber, itemImageUrls, itemImageCheckUrls } = props;

  return (
    <Container>
      <div className="img-title">검수용 사진</div>
      <ImageSlider>
        {itemImageCheckUrls.map((image: ImageUrl) => (
          <img key={image.id} src={image.url} alt="image" />
        ))}
      </ImageSlider>
      <div className="img-title">제품 사진</div>
      <ImageSlider>
        {itemImageUrls.map((image: ImageUrl) => (
          <img key={image.id} src={image.url} alt="image" />
        ))}
      </ImageSlider>
      <ProductName>{itemName}</ProductName>
    </Container>
  );
}

const Container = styled.div`
  margin-bottom: 42px;
  .img-title {
    margin: 36px 0px 10px 0px;
  }
  img {
    height: 255px;
    object-fit: cover;
  }
`;

const ProductName = styled.h1`
  font-size: var(--font-x-large);
  margin-top: 32px;
  margin-bottom: 6px;
  letter-spacing: normal;
  width: 100%;
  white-space: pre-line;
  line-height: 1.2;
`;

const LotNumber = styled.span`
  color: var(--color-brown300);
  font-size: var(--font-x-micro);
  letter-spacing: normal;
`;

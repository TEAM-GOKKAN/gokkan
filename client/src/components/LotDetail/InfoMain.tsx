import React from 'react';
import styled from 'styled-components';
import ImageSlider from '../common/ImageSlider';

interface Iprops {
  lotName: string;
  lotNumber: string;
  lotImageUrls: ImageUrl[];
}

interface ImageUrl {
  id: number;
  url: string;
}

export default function InfoMain(props: Iprops) {
  const { lotName, lotNumber, lotImageUrls } = props;

  return (
    <Container>
      <ImageSlider>
        {lotImageUrls.length &&
          lotImageUrls.map((image: ImageUrl) => (
            <img key={image.id} src={image.url} alt="image" />
          ))}
      </ImageSlider>
      <ProductName>{lotName}</ProductName>
      <LotNumber>{`No.${lotNumber}`}</LotNumber>
    </Container>
  );
}

const Container = styled.div`
  margin-bottom: 42px;
`;

const ProductName = styled.h1`
  font-size: var(--font-x-large);
  line-height: calc(var(--font-x-large) * 1.2);
  margin-top: 32px;
  margin-bottom: 6px;
  letter-spacing: normal;
  width: 100%;
  white-space: pre-line;
`;

const LotNumber = styled.span`
  color: var(--color-brown300);
  font-size: var(--font-x-micro);
  letter-spacing: normal;
`;

import React from 'react';
import styled from 'styled-components';

interface Iprops {
  brand: string;
  designer: string;
  material: string;
  period: number;
  size: string;
  conditionGrade: string;
  conditionDescription: string | undefined;
}

export default function InfoDetail(props: Iprops) {
  const {
    brand,
    designer,
    material,
    period,
    size,
    conditionGrade,
    conditionDescription,
  } = props;

  return (
    <Container>
      <Row>
        <InfoTitle>Brand</InfoTitle>
        <InfoContent>{brand || 'unknown'}</InfoContent>
      </Row>
      <Row>
        <InfoTitle>Designer</InfoTitle>
        <InfoContent>{brand || 'unknown'}</InfoContent>
      </Row>
      <Row>
        <InfoTitle>Material</InfoTitle>
        <InfoContent>{material}</InfoContent>
      </Row>
      <Row>
        <InfoTitle>Period</InfoTitle>
        <InfoContent>{period}</InfoContent>
      </Row>
      <Row>
        <InfoTitle>Size</InfoTitle>
        <InfoContent>{size}</InfoContent>
      </Row>
      <Row>
        <InfoTitle>Condition</InfoTitle>
        <InfoContent>
          <span>{conditionGrade}</span>
          <span>{' - ' + conditionDescription}</span>
        </InfoContent>
      </Row>
    </Container>
  );
}

const Container = styled.ul`
  margin-bottom: 42px;

  &::after {
    content: '';
    width: 100%;
    height: 1px;
    margin-top: 42px;
    display: block;
    border-bottom: 1px solid var(--color-brown100);
  }
`;

const Row = styled.li`
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 20px;
  /* position: relative; */
  width: 100%;
  /* background: blue; */
`;

const InfoTitle = styled.span`
  color: var(--color-brown300);
  font-size: var(--font-micro);
  letter-spacing: normal;
  width: calc(50% - 8px);
  line-height: calc(var(--font-regular) * 1.4);
`;

const InfoContent = styled.span`
  /* position: absolute; */
  /* left: calc(50% + 8px); */
  width: calc(50% - 8px);
  white-space: pre-line;
  line-height: calc(var(--font-regular) * 1.4);
`;

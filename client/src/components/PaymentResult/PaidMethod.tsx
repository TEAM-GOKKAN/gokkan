import React from 'react';
import styled from 'styled-components';
import { PGS } from '../../lib/constants/payment';

interface IProps {
  pg: string;
}

interface IParams {
  value: string;
  label: string;
}

export default function PaidMethod({ pg }: IProps) {
  const method = PGS.find((method) => method.value === pg);
  return (
    <SectionContainer>
      <TitleContainer>
        <Title>결제수단</Title>
        <Content>{method?.label}</Content>
      </TitleContainer>
    </SectionContainer>
  );
}

const SectionContainer = styled.section`
  width: 100%;
  margin-bottom: 24px;
  font-size: var(--font-small);

  &:not(.first)::before {
    content: '';
    display: inline-block;
    margin-bottom: 24px;
    border-bottom: 1px solid var(--color-brown100);
    width: calc(100% + 32px);
    position: relative;
    right: 16px;
  }
`;

const TitleContainer = styled.div`
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const Title = styled.h3`
  font-size: var(--font-regular);
  font-weight: var(--weight-bold);
`;

const Content = styled.span`
  white-space: nowrap;
  overflow: hidden;
  color: var(--color-brown300);
`;

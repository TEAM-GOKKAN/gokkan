import React from 'react';
import styled from 'styled-components';
import { VscHeart } from 'react-icons/vsc';

export default function Favorite() {
  return (
    <Container>
      <VscHeart size="18" />
      <span>46</span>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  gap: 4px;
  align-items: center;
  margin-bottom: 42px;

  & > span {
    letter-spacing: normal;
    font-size: var(--font-small);
  }
`;

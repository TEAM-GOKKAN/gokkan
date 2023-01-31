import React from 'react';
import styled from 'styled-components';
import { FiChevronDown } from 'react-icons/fi';

interface Iprops {
  content: string;
}

export default function LotDescription(props: Iprops) {
  return (
    <Container>
      <Description>{props.content}</Description>
      {/* <ShowMoreButton>
        <span>더보기</span>
        <FiChevronDown size="14" />
      </ShowMoreButton> */}
    </Container>
  );
}

const Container = styled.div`
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

const Description = styled.p`
  font-size: var(--font-small);
  line-height: calc(var(--font-small) * 1.6);
  white-space: pre-line;
`;

const ShowMoreButton = styled.button`
  padding: 0;
  margin-top: 16px;
  display: flex;
  gap: 2px;

  & > span {
    font-weight: 500;
  }
`;

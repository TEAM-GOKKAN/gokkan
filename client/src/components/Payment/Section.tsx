import React, { useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import { FiChevronDown } from 'react-icons/fi';

interface IProps {
  title?: string;
  preview?: string;
  children: React.ReactNode;
}

export default function Section({ title, preview, children }: IProps) {
  const [isToggleOpen, setIsToggleOpen] = useState(true);
  const containerRef = useRef(null);

  const handleClickToggle = () => {
    setIsToggleOpen(!isToggleOpen);
  };

  return (
    <SectionContainer ref={containerRef}>
      {title && (
        <TitleContainer>
          <Title>{title}</Title>
          {!isToggleOpen && <PreviewContent>{preview}</PreviewContent>}
          <ToggleButton type="button" onClick={handleClickToggle}>
            <FiChevronDown size="18" />
          </ToggleButton>
        </TitleContainer>
      )}
      {isToggleOpen && children}
    </SectionContainer>
  );
}

const SectionContainer = styled.section`
  width: 100%;
  margin-bottom: 24px;
  font-size: var(--font-small);

  &::before {
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

const PreviewContent = styled.span`
  max-width: 50%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: var(--color-brown300);
  position: absolute;
  right: 48px;
`;

const ToggleButton = styled.button`
  width: 18px;
  height: 18px;
`;

import React from 'react';
import { BarLoader } from 'react-spinners';
import styled from 'styled-components';

export default function LoadingSpinner() {
  return (
    <LoaderWrapper>
      <BarLoader color="#9D9792" width="80px" />
    </LoaderWrapper>
  );
}

const LoaderWrapper = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-white);
  z-index: 9999;
`;

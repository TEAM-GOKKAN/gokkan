import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const MyReturnedLot = () => {
  const navigate = useNavigate();

  const handleReturnedLotClick = () => {
    navigate('/myReturnedLot');
  };

  return <Container onClick={handleReturnedLotClick}>반려된 경매</Container>;
};

export default MyReturnedLot;

const Container = styled.div`
  width: 100%;
`;

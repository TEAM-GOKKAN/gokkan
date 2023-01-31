import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const MyFinishedBidLot = () => {
  const navigate = useNavigate();

  const handleFinishedBidLotClick = () => {
    navigate('/myFinishedBidLot');
  };

  return (
    <Container onClick={handleFinishedBidLotClick}>결제완료된 경매</Container>
  );
};

export default MyFinishedBidLot;

const Container = styled.div`
  width: 100%;
`;

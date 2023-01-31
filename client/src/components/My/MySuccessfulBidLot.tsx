import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const MySuccessfulBidLot = () => {
  const navigate = useNavigate();

  const handleSuccessfulBidLotClick = () => {
    navigate('/mySuccessfulBidLot');
  };

  return (
    <Container onClick={handleSuccessfulBidLotClick}>낙찰된 경매</Container>
  );
};

export default MySuccessfulBidLot;

const Container = styled.div`
  width: 100%;
`;

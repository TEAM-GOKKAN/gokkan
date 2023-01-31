import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const MyBidLot = () => {
  const navigate = useNavigate();

  const handleBidLotClick = () => {
    navigate('/myBidLot');
  };

  return <Container onClick={handleBidLotClick}>입찰한 경매</Container>;
};

export default MyBidLot;

const Container = styled.div`
  width: 100%;
`;

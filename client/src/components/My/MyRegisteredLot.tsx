import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const MyRegisteredLot = () => {
  const navigate = useNavigate();

  const handleRegisterdLotClick = () => {
    navigate('/myRegisteredLot');
  };

  return <Container onClick={handleRegisterdLotClick}>등록한 경매</Container>;
};

export default MyRegisteredLot;

const Container = styled.div`
  width: 100%;
`;

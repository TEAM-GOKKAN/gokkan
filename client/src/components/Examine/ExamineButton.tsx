import React from 'react';
import styled from 'styled-components';
import { useNavigate, useLocation } from 'react-router-dom';

interface Iprops {
  itemId: number;
}

const ExamineButton = (props: Iprops) => {
  const { itemId } = props;

  const navigate = useNavigate();
  const location = useLocation();

  const handleExamineButtonClick = () => {
    navigate(`/examine/${itemId}`, { state: { background: location } });
  };

  return <Container onClick={handleExamineButtonClick}>감정평가</Container>;
};

export default ExamineButton;

const Container = styled.button`
  width: 100%;
  height: 50px;
  position: fixed;
  bottom: 0;
  left: 0;
  background-color: var(--color-brown500);
  color: var(--color-brown100);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: var(--font-regular);
  font-weight: var(--weight-semi-bold);
`;

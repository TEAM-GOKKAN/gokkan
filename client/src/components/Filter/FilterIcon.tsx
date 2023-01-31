import React from 'react';
import { BiFilterAlt } from 'react-icons/bi';
import styled from 'styled-components';
import { useLocation, useNavigate } from 'react-router-dom';

const FilterIcon = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleFilterButtonClick = () => {
    navigate('/filter', { state: { background: location } });
  };
  return (
    <Container onClick={handleFilterButtonClick}>
      <BiFilterAlt />
      <div className="title">필터</div>
    </Container>
  );
};

export default FilterIcon;

const Container = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: right;
  align-items: center;
  color: var(--color-brown400);
  .title {
    margin-left: 3px;
    font-size: 14px;
    font-weight: 400;
    line-height: 14px;
    letter-spacing: -4%;
  }
  svg {
    font-size: 14px;
    line-height: 14px;
  }
`;

import React, { useState } from 'react';
import styled from 'styled-components';
import { HiOutlineMenu } from 'react-icons/hi';
import { useLocation, useNavigate } from 'react-router-dom';

const IconButton = styled.button`
  padding: 0;
  display: flex;

  & > * {
    height: 100%;
  }
`;

const CategoryButton = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleSignInButttonClick = () => {
    navigate('/categoryModal', { state: { background: location } });
  };

  return (
    <IconButton onClick={handleSignInButttonClick}>
      <HiOutlineMenu size="22" color="var(--color-brown300)" />
    </IconButton>
  );
};

export default CategoryButton;

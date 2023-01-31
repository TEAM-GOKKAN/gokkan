import React, { useState } from 'react';
import styled from 'styled-components';
import { HiOutlineUser } from 'react-icons/hi';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';

const IconButton = styled.button`
  padding: 0;
  display: flex;

  & > * {
    height: 100%;
  }
`;

const SignInButton = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  const handleSignInButttonClick = () => {
    if (accessToken !== '') {
      navigate('/myPage', { state: { background: location } });
    } else {
      navigate('/signin', { state: { background: location } });
    }
  };

  return (
    <IconButton onClick={handleSignInButttonClick}>
      <HiOutlineUser size="22" color="var(--color-brown300)" />
    </IconButton>
  );
};

export default SignInButton;

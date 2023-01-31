import React from 'react';
import styled from 'styled-components';
import logo from '../../assets/logo/logo.png';
import SignInButton from './SignInButton';
import CategoryButton from './CategoryButton';
import { Link } from 'react-router-dom';

export default function Header() {
  return (
    <Container>
      <Fixed>
        <CategoryButton />
        <LogoContainer>
          <Link to="/">
            <img src={logo} alt="gokkan" />
          </Link>
        </LogoContainer>
        <SignInButton />
      </Fixed>
    </Container>
  );
}

const Container = styled.header`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  height: 60px;
  background: var(--color-white);
  z-index: 9990;
`;

const Fixed = styled.div`
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100%;
`;

const IconButton = styled.button`
  padding: 0;
  display: flex;
`;

const LogoContainer = styled.div`
  cursor: pointer;
  height: 100%;

  & img {
    height: 100%;
    object-fit: cover;
  }
`;

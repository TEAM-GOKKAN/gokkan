import React, { useEffect, useState } from 'react';
import useGetToken from '../components/SignUp/useGetToken';
import UserName from '../components/SignUp/UserName';
import UserNickName from '../components/SignUp/UserNickName';
import UserAddress from '../components/SignUp/UserAddress';
import UserProfileImageContainer from '../components/SignUp/UserProfileImageContainer';
import UserPhone from '../components/SignUp/UserPhone';
import SingUpConfirmButton from '../components/SignUp/SingUpConfirmButton';
import useGetUserInfo from '../components/SignUp/useGetUserInfo';
import styled from 'styled-components';

const SignUpWrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
`;

const SignUpPage = () => {
  useGetToken();
  useGetUserInfo();
  return (
    <SignUpWrapper>
      <UserName />
      <UserNickName />
      <UserPhone />
      <UserAddress />
      <UserProfileImageContainer />
      <SingUpConfirmButton />
    </SignUpWrapper>
  );
};

export default SignUpPage;

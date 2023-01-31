import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import customAxios from '../../utils/customAxios';
import { useNavigate } from 'react-router-dom';

const LogOut = () => {
  const navigate = useNavigate();

  const logOutClick = () => {
    const url = 'api/v1/users/logout';
    customAxios
      .post(url)
      .then(({ data }) => {})
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        localStorage.setItem('accessToken', '');
        localStorage.setItem('refreshToken', '');
        navigate('/');
      });
  };

  return <SpreadList title="로그아웃" clickFn={logOutClick} />;
};

export default LogOut;

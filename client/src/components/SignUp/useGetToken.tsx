import React, { useState, useEffect } from 'react';
import queryString from 'query-string';

const useGetAccessToken = () => {
  useEffect(() => {
    const queryParam = queryString.parse(window.location.search);
    const token = queryParam?.token;
    const refreshToken = queryParam?.refreshToken;
    if (token) {
      localStorage.setItem('accessToken', String(token));
    }
    if (refreshToken) {
      localStorage.setItem('refreshToken', String(refreshToken));
    }
  }, []);
};

export default useGetAccessToken;

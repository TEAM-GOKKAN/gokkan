import axios, { AxiosInstance } from 'axios';

const url = 'http://3.38.59.40:8080/';
let localAccessToken = localStorage.getItem('accessToken');
let localRefreshToken = localStorage.getItem('refreshToken');
const customAxios: AxiosInstance = axios.create({
  baseURL: `${url}`,
});

customAxios.interceptors.request.use(
  (config) => {
    if (localAccessToken) {
      if (!config.headers) return config;
      if (!(config.headers as any).Authorization) {
        (config.headers as any)['Authorization'] =
          'Bearer ' + localAccessToken.replace(/"/g, '');
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

customAxios.interceptors.response.use(
  (res) => {
    return res;
  },
  async (err) => {
    const originalConfig = err.config;
    if (originalConfig.url !== '/auth/signin' && err.response) {
      // Access Token was expired
      if (err.response.status === 401 && !originalConfig._retry) {
        originalConfig._retry = true;
        try {
          const newAxios = axios.create();
          delete newAxios.defaults.headers.common['Authorization'];

          const rs = await newAxios.get(
            'http://3.38.59.40:8080/api/v1/auth/refresh',
            {
              params: {
                refreshToken: localRefreshToken?.replace(/"/g, ''),
              },
            }
          );
          // accessToken 갱신
          const { accessToken, refreshToken } = rs.data;
          localStorage.setItem('accessToken', accessToken);
          localStorage.setItem('refreshToken', refreshToken);
          localAccessToken = accessToken;
          localRefreshToken = refreshToken;
          originalConfig.headers.Authorization = 'Bearer ' + accessToken;
          return customAxios(originalConfig);
        } catch (_error) {
          // refreshToken이 만료되었을 때, 로그아웃이 되도록 함
          // 토큰 지우고, 홈페이지로 redirect 시킴
          localStorage.setItem('accessToken', '');
          localStorage.setItem('refreshToken', '');
          window.location.replace('/');
          return Promise.reject(_error);
        }
      }
    }
    return Promise.reject(err);
  }
);

export default customAxios;

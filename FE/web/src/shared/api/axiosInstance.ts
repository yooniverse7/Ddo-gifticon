import axios from 'axios';
import { BASE_URL } from '../constants/url';
// import { useAuthStore } from '@/store/auth';

// const { accessToken } = useAuthStore.getState();
// console.log(accessToken);

export const axiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'xx-auth': 'acc-tkn',
  },
});

// 토큰 설정을 위한 함수
export const setAuthToken = (token: string | null) => {
  if (token) {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete axiosInstance.defaults.headers.common['Authorization'];
  }
};

// 요청 인터셉터 추가
axiosInstance.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

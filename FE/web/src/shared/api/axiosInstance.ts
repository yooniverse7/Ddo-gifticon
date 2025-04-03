import axios from 'axios';
import { BASE_URL } from '../constants/url';
import { useAuthStore } from '@/store/auth';

const { accessToken } = useAuthStore.getState();
console.log(accessToken);

export const axiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'xx-auth': 'acc-tkn',
  },
});

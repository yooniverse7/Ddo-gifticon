import axios from 'axios';
import { BASE_URL } from '../constants/url';

export const axiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
});

import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TGift } from '../model/gift';

export const useFetchNFC = () => {
  // TODO: 결제 대기 상태로 기다리다가 결제 완료 response 받으면 return 하기
};

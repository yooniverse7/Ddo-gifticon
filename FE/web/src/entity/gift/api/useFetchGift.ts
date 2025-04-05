import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TGiftResponse } from '../model/gift';

export const useFetchGift = () => {
  return useQuery<TGiftResponse>({
    queryKey: ['gifts'],
    queryFn: async () => {
      const response = await axiosInstance.get<TGiftResponse>(API_URL.gift);
      return response.data;
    },
  });
};

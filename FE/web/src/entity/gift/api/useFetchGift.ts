import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TGift } from '../model/gift';

export const useFetchGift = () => {
  const { data: gifts = [] } = useQuery<TGift[]>({
    queryKey: ['gifts'],
    queryFn: async () => {
      const response = await axiosInstance.get(API_URL.gift);
      return response.data;
    },
  });

  return { gifts };
};

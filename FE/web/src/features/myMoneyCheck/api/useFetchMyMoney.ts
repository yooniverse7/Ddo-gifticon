import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';

interface balanceResponse {
  pay_balance: number;
}

export const useFetchBalance = () => {
  const { data: balance } = useQuery<balanceResponse>({
    queryKey: ['balance'],
    queryFn: async () => {
      const response = await axiosInstance.get(API_URL.pay_balance);
      return response.data.content;
    },
  });
  return { balance };
};

import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';

interface MoneyResponse {
  status: {
    code: number;
    message: string;
  };
  content: {
    pay_balance: number;
  };
}

export function useFetchMyMoney() {
  return useQuery<MoneyResponse>({
    queryKey: ['myMoney'],
    queryFn: async () => {
      const response = await axiosInstance.get<MoneyResponse>(API_URL.pay_balance);
      return response.data;
    },
  });
}

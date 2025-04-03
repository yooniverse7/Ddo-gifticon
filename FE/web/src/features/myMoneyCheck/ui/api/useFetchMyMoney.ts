import { axiosInstance } from '@/shared/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';

interface MoneyResponse {
  payBalance: number;
}

export function useFetchMyMoney() {
  return useQuery<MoneyResponse>({
    queryKey: ['myMoney'],
    queryFn: async () => {
      const response = await axiosInstance.get<MoneyResponse>(
        '/api/pay/balance'
      );
      return response.data;
    },
  });
}

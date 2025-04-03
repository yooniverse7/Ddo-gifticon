import { axiosInstance } from '@/shared/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';
import { TPayment } from '@/entity/store/model/payment';

export const usePaymentList = () => {
  return useQuery<TPayment[]>({
    queryKey: ['paymentList'],
    queryFn: async () => {
      const response = await axiosInstance.get<TPayment[]>('/api/pay/history');
      return response.data;
    },
  });
};

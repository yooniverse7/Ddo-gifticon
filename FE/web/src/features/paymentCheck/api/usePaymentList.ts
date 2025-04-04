import { axiosInstance } from '@/shared/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';
import { TPayment } from '@/entity/store/model/payment';
import { API_URL } from '@/shared/constants/url';

export const usePaymentList = () => {
  return useQuery<TPayment>({
    queryKey: ['paymentList'],
    queryFn: async () => {
      const response = await axiosInstance.get<TPayment>(
        `${API_URL.pay_list}?history_type=BALANCE`
      );
      return response.data;
    },
  });
};

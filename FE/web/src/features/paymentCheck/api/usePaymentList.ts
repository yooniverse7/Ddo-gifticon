import { axiosInstance } from '@/shared/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';
import { TPaymentResponse } from '@/entity/store/model/payment';
import { API_URL } from '@/shared/constants/url';

export const usePaymentList = () => {
  return useQuery<TPaymentResponse>({
    queryKey: ['paymentList'],
    queryFn: async () => {
      const response = await axiosInstance.get<TPaymentResponse>(
        `${API_URL.pay_list}?history_type=BALANCE`
      );
      console.log(response.data);
      return response.data;
    },
  });
};

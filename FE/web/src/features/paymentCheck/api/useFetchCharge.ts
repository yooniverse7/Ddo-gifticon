import { TCharge } from '@/entity/store/model/charge';
import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';

export const useFetchCharge = () => {
  return useQuery<TCharge>({
    queryKey: ['charge'],
    queryFn: async () => {
      const response = await axiosInstance.post<TCharge>(`${API_URL.charge}`, {
        amount: amount,
      });
      return response.data;
    },
  });
};

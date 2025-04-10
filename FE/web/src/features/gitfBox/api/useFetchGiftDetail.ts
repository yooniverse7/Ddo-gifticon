import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TGiftDetail } from '../model/giftDetail';

export default function useFetchGiftDetail(giftId: number) {
  const { data: giftDetail } = useQuery<TGiftDetail>({
    queryKey: ['giftDetail'],
    queryFn: async () => {
      const response = await axiosInstance.get(
        `${API_URL.giftDetail}/${giftId}`
      );
      return response.data.content;
    },
  });
  return { giftDetail };
}

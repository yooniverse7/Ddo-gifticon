import { TSentGift } from '@/entity/gift/model/gift';
import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';

export default function useFetchSentGift() {
  const { data: sentGifts } = useQuery<TSentGift[]>({
    queryKey: ['sentGifts'],
    queryFn: async () => {
      const response = await axiosInstance.get(`${API_URL.sentGifts}`);
      return response.data.content;
    },
  });
  return sentGifts;
}

import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TMenu } from '@/entity/store/model/menu';

export const useFetchMenu = (id: number) => {
  return useQuery<TMenu>({
    queryKey: ['menu'],
    queryFn: async () => {
      const response = await axiosInstance.get<TMenu>(`${API_URL.favoriteStores}/${id}`);
      return response.data;
    },
  });
};

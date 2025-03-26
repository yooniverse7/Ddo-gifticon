import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { FavoriteStores } from '../model/stores';

export const useFetchFavoriteStores = () => {
  const { data: favoriteStores } = useQuery<FavoriteStores[]>({
    queryKey: ['favoriteStores'],
    queryFn: async () => {
      const response = await axiosInstance.get(API_URL.favoriteStores);
      return response.data;
    },
  });

  return { favoriteStores };
};

import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TFavoriteStores } from '../model/stores';
import { useMapStore } from '@/store/useMapStore';

export const useFetchFavoriteStores = () => {
  const { map } = useMapStore();
  const lat = map?.getCenter().getLat() ?? 0;
  const lng = map?.getCenter().getLng() ?? 0;

  const { data: favoriteStores = [] } = useQuery<TFavoriteStores[]>({
    queryKey: ['favoriteStores'],
    queryFn: async () => {
      const response = await axiosInstance.get(`${API_URL.favoriteStores}?lat=${lat}&lng=${lng}`);
      return response.data;
    },
  });

  return { favoriteStores };
};

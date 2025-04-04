import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery } from '@tanstack/react-query';
import { TMarketResponse } from '../model/menu';
import { useMapStore } from '@/store/useMapStore';

export const useFetchFavoriteStores = () => {
  const { map } = useMapStore();
  const lat = map?.getCenter().getLat() ?? 0;
  const lng = map?.getCenter().getLng() ?? 0;

  const { data: favoriteStores = [] } = useQuery<TMarketResponse[]>({
    queryKey: ['favoriteStores'],
    queryFn: async () => {
      const response = await axiosInstance.get(`${API_URL.favoriteStores}?lat=${lat}&lng=${lng}`);
      console.log(response.data);
      return response.data.content;
    },
  });

  return { favoriteStores };
};

import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useQuery, UseQueryOptions } from '@tanstack/react-query';
import { TMenu, TMarketResponse, TCustomMenu } from '@/entity/store/model/menu';

type MenuData = {
  menu: TMenu[];
  customMenu: TCustomMenu[];
};

export const useFetchMenu = (
  id: number,
  options?: Omit<UseQueryOptions<MenuData, Error, MenuData>, 'queryKey' | 'queryFn'>
) => {
  return useQuery<MenuData>({
    queryKey: ['menu', id],
    queryFn: async () => {
      const response = await axiosInstance.get<TMarketResponse>(`${API_URL.favoriteStores}/${id}`);
      console.log('메뉴 데이터:', response.data);
      return {
        menu: response?.data.content?.menu ?? [],
        customMenu: response?.data.content?.custom_menu ?? [],
      };
    },
    ...options,
  });
};

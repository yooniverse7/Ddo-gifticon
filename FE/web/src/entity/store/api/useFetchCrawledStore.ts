import { axiosInstance } from '@/shared/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';

type Menu = {
  menu_name: string;
  menu_desc: string;
  menu_price: string;
  menu_image: string;
};

type Position = {
  lat: number;
  lng: number;
};

export type CrawledData = {
  id: null;
  place_name: string;
  main_image_url: string;
  address_name: string;
  position: Position;
  store_info: string;
  star_rating: string;
  place_id: string;
  menus: Menu[];
};

export const useFetchCrawledStore = (data: any) => {
  const { data: crawledData } = useQuery<CrawledData>({
    queryKey: ['crawledStore'],
    queryFn: async () => {
      const res = await axiosInstance.get(`/api/restaurants/crawling?data=${data}`);
      return res.data.stores[0];
    },
  });

  return { crawledData };
};

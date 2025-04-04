import { axiosInstance } from '@/shared/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';
import { encodeUrl } from '@/shared/utils/encodeUrl';

export const useFetchCrawledStore = (data: any) => {
  const { data: crawledData } = useQuery({
    queryKey: ['crawledStore'],
    queryFn: () => {
      return axiosInstance.get(`/api/restaurants/crawling?data=${encodeUrl(JSON.stringify(data))}`);
    },
  });

  return { crawledData };
};

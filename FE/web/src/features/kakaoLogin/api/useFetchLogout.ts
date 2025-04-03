import { axiosInstance } from "@/shared/api/axiosInstance";
import { useQuery } from "@tanstack/react-query";

export const useFetchLogout = () => {
  return useQuery({
    queryKey: ['logout'],
    queryFn: async () => {
      const response = await axiosInstance.post('/api/users/logout');
      return response.data;
    },
  });
};

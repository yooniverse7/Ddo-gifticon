import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';

export const useSendCreateFavoriteStore = () => {
  const router = useRouter();

  const { mutate: mutateCreateFavoriteStore } = useMutation({
    mutationFn: (body) => {
      return axiosInstance.post(API_URL.favoriteStores, body);
    },
    onSuccess: (data) => {
      console.log(data);
      //TODO - loading 후
      router.push('/');
    },
  });

  return { mutateCreateFavoriteStore };
};

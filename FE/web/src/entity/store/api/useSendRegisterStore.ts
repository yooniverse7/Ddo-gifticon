import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';

export const useSendRegisterStore = () => {
  const router = useRouter();

  const { mutate: mutateSendRegisterStore } = useMutation({
    mutationFn: async (formData: FormData) => {
      return axiosInstance.post(API_URL.favoriteStores, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
    },
    onSuccess: () => {
      router.push('/');
    },
    onError: (error) => {
      console.error('오류 :', error);
    },
  });

  return { mutateSendRegisterStore };
};

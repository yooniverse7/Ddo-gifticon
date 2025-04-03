'use client';

import { useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { fetchKakaoLogin } from '../api/useFetchLogin';
import { useAuthStore } from '@/store/auth';

export default function KakaoCallback() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const code = searchParams.get('code');
  const { setTokens } = useAuthStore();

  useEffect(() => {
    const handleLogin = async () => {
      if (!code) {
        router.push('/login');
        return;
      }

      try {
        const data = await fetchKakaoLogin(code);
        if (data.accessToken) {
          // 일반 쿠키로 토큰 저장
          document.cookie = `accessToken=${data.accessToken}; path=/`;
          // 위 코드에서 path=/ 는 쿠키가 모든 경로에서 사용 가능하도록 설정합니다.
          // 이제 zustand의 setTokens 함수를 호출하여 토큰을 저장합니다.
          setTokens(data.accessToken);
          router.push('/');
        } else {
          throw new Error('토큰이 없습니다.');
        }
      } catch (error) {
        console.error('로그인 처리 중 오류 발생:', error);
        router.push('/login');
      }
    };

    handleLogin();
  }, [code, router]);

  return (
    <div className='min-h-screen flex items-center justify-center bg-gray-50'>
      <div className='text-center'>
        <h2 className='text-xl font-semibold text-gray-900 mb-2'>로그인 처리 중...</h2>
        <p className='text-gray-600'>잠시만 기다려주세요.</p>
      </div>
    </div>
  );
}

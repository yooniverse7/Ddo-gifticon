'use client';

import { useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { fetchKakaoLogin } from '../api/useFetchLogin';

export default function KakaoCallback() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const code = searchParams.get('code');

  useEffect(() => {
    const handleLogin = async () => {
      if (!code) {
        router.push('/login');
        return;
      }

      try {
        const data = await fetchKakaoLogin(code);
        if (data.accessToken) {
          // 토큰을 localStorage나 쿠키에 저장
          localStorage.setItem('accessToken', data.accessToken);
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
        <h2 className='text-xl font-semibold text-gray-900 mb-2'>
          로그인 처리 중...
        </h2>
        <p className='text-gray-600'>잠시만 기다려주세요.</p>
      </div>
    </div>
  );
}

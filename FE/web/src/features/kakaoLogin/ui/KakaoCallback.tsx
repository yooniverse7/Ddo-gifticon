'use client';

import { useEffect, Suspense } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { fetchKakaoLogin } from '../api/useFetchLogin';
import { useAuthStore } from '@/store/auth';

function KakaoCallbackContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const code = searchParams.get('code');
  const { setTokens } = useAuthStore();

  useEffect(() => {
    console.log('code', code);
    const handleLogin = async () => {
      if (!code) {
        router.push('/login');
        return;
      }

      try {
        const data = await fetchKakaoLogin(code);
        console.log('data', data);
        if (data.accessToken) {
          console.log('data.accessToken', data.accessToken);
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

export default function KakaoCallback() {
  return (
    <Suspense
      fallback={
        <div className='min-h-screen flex items-center justify-center bg-gray-50'>
          <div className='text-center'>
            <h2 className='text-xl font-semibold text-gray-900 mb-2'>로딩 중...</h2>
            <p className='text-gray-600'>잠시만 기다려주세요.</p>
          </div>
        </div>
      }
    >
      <KakaoCallbackContent />
    </Suspense>
  );
}

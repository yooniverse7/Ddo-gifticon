'use client';

import { Button } from '@/components/ui/button';

export default function Login() {
  const REST_API_KEY = process.env.NEXT_PUBLIC_KAKAO_REST_API_KEY;
  const REDIRECT_URI = process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI;
  const link = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

  const handleSignIn = () => {
    window.location.href = link;
  };

  return (
    <div className='min-h-screen flex items-center justify-center bg-gray-50'>
      <div className='max-w-md w-full bg-white rounded-2xl shadow-sm p-8 space-y-6 text-center'>
        <div className='space-y-2'>
          <h1 className='text-2xl font-bold text-gray-900'>
            안녕하세요 또가게입니다.
          </h1>
          <h2 className='text-gray-600'>
            지금 카카오로 간편하게 로그인하세요!
          </h2>
        </div>
        <Button
          onClick={handleSignIn}
          className='w-full py-6 text-lg font-medium bg-[#FEE500] hover:bg-[#FEE500]/90 text-[#391B1B]'
        >
          카카오로 로그인
        </Button>
      </div>
    </div>
  );
}

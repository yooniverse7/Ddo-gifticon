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
    <>
      <div>
        <h1>안녕하세요 또가게입니다.</h1>
        <h2>지금 카카오로 간편하게 로그인하세요!</h2>
        <Button onClick={handleSignIn}>카카오로 로그인</Button>
      </div>
    </>
  );
}

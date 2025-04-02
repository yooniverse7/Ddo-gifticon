// pages/kakao.jsx 또는 pages/kakao.tsx
import { useEffect } from 'react';
import { useRouter } from 'next/router';
import useFetchLogin from '../api/useFetchLogin';

export default function KakaoCallback() {
  const router = useRouter();

  useEffect(() => {
    const handleLogin = async () => {
      // URL에서 인가 코드 추출
      const code = router.query.code as string;

      if (code) {
        try {
          // 인가 코드가 있으면 서버로 전송하여 토큰 획득
          await useFetchLogin(code);
          // useFetchLogin 내부에서 이미 리다이렉트 처리가 되어있으므로 추가 작업 불필요
        } catch (error) {
          console.error('로그인 처리 중 오류 발생:', error);
          // 에러 발생 시 로그인 페이지로 리다이렉트
          router.push('/login');
        }
      }
    };

    handleLogin();
  }, [router.query]);

  return (
    <div>
      <p>카카오 로그인 처리 중...</p>
    </div>
  );
}

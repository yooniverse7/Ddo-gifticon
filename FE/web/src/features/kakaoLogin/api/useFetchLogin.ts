import { useState } from 'react';
import { useRouter } from 'next/navigation';
async function useFetchLogin(code: string) {
  const router = useRouter();
  const [login, setLogin] = useState(false);
  try {
    const response = await fetch(
      'http://localhost:8080/api/users/social/kakao/login',
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code }),
      }
    );
    const data = await response.json();
    if (data.success) {
      setLogin(true);
      router.push('/');
    } else {
      console.error('로그인 실패:', data.message);
    }
  } catch (error) {
    console.error('로그인 처리 중 오류', error);
  }
  return { login, setLogin };
}

export default useFetchLogin;

import axios from 'axios';

export async function fetchKakaoLogin(code: string) {
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_BASE_URL}/api/auth/kakao/callback`,
      {
        code,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'XX-Auth': 'acc-tkn',
        },
        withCredentials: true,
      }
    );
    if (!response.data.accessToken) {
      console.error('토큰이 응답에 없습니다.');
      throw new Error('토큰이 없습니다.');
    }
    return response.data;
  } catch (error) {
    console.error('카카오 로그인 처리 중 오류:', error);
    throw error;
  }
}

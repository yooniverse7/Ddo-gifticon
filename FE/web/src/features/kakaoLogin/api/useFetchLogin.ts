export async function fetchKakaoLogin(code: string) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_BASE_URL}/api/auth/kakao/callback`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code }),
      }
    );

    if (!response.ok) {
      throw new Error('로그인 실패');
    }

    const data = await response.json();
    console.log("Social login response:", data); // 응답 전체를 콘솔에 출력

    // 예시: accessToken이 있는지 확인
    if (!data.accessToken) {
      throw new Error("토큰이 없습니다.");
    }

    return data;
    } catch (error) {
    console.error("로그인 처리 중 오류", error);
    throw error;
    }
}

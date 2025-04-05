'use client';

import { Button } from '@/components/ui/button';
import Image from 'next/image';

export default function Login() {
  const REST_API_KEY = process.env.NEXT_PUBLIC_KAKAO_REST_API_KEY;
  const REDIRECT_URI = process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI;
  const kakaoLink = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

  const handleKakaoSignIn = () => {
    window.location.href = kakaoLink;
  };

  // 추가 소셜 로그인 핸들러들 (실제 구현은 각각의 API에 맞게 수정 필요)
  const handleGoogleSignIn = () => {
    // Google 로그인 로직
    console.log('Google 로그인');
  };

  const handleNaverSignIn = () => {
    // Naver 로그인 로직
    console.log('Naver 로그인');
  };

  return (
    <div className='min-h-screen flex flex-col items-center justify-center bg-white'>
      <div className='w-full max-w-md px-6 flex flex-col items-center'>
        <Image src='/splash_image.png' alt='logo' width={64} height={64} />
        <div className='mb-10 flex flex-col items-center'>
          {/* 여기에 로고 이미지가 들어갑니다 */}
          <div className='text-3xl font-bold mb-2 mt-4'>또가게</div>
        </div>

        {/* 안내 텍스트 */}
        <div className='w-full text-left mb-6'>
          <h2 className='text-xl font-medium text-black mb-1'>
            아직 회원이 아니신가요?
          </h2>
          <h3 className='text-xl font-bold text-black'>3초만에 로그인 하기</h3>
        </div>

        {/* 로그인 버튼 영역 */}
        <div className='w-full space-y-3'>
          {/* 카카오 로그인 버튼 */}
          <Button
            onClick={handleKakaoSignIn}
            className='w-full py-6 text-base font-medium bg-[#FFE500] hover:bg-[#FFE500]/90 text-black rounded-md flex items-center justify-center'
          >
            <span className='mr-2'>
              <svg
                width='28'
                height='28'
                viewBox='0 0 24 24'
                fill='none'
                xmlns='http://www.w3.org/2000/svg'
              >
                <path
                  fillRule='evenodd'
                  clipRule='evenodd'
                  d='M12 4C8.13 4 5 6.5 5 9.65C5 11.7 6.35 13.5 8.38 14.54L7.5 17.5C7.45 17.65 7.52 17.82 7.66 17.9C7.74 17.95 7.83 17.97 7.92 17.97C8.01 17.97 8.09 17.95 8.17 17.9L11.62 15.68C11.75 15.69 11.88 15.7 12 15.7C15.87 15.7 19 13.2 19 10.05C19 6.9 15.87 4 12 4Z'
                  fill='black'
                />
              </svg>
            </span>
            카카오 로그인
          </Button>

          {/* 구글 로그인 버튼 */}
          <button
            onClick={handleGoogleSignIn}
            className='w-full py-6 text-base font-medium bg-gray-100 hover:bg-gray-200 text-black rounded-md flex items-center justify-center'
          >
            <span className='mr-2'>
              <svg
                width='18'
                height='18'
                viewBox='0 0 18 18'
                fill='none'
                xmlns='http://www.w3.org/2000/svg'
              >
                <path
                  d='M17.64 9.20455C17.64 8.56637 17.5827 7.95 17.4764 7.36364H9V10.845H13.8436C13.635 11.97 13.0009 12.9232 12.0477 13.5614V15.8195H14.9564C16.6582 14.2527 17.64 11.9455 17.64 9.20455Z'
                  fill='#4285F4'
                />
                <path
                  d='M9 18C11.43 18 13.4673 17.1941 14.9564 15.8195L12.0477 13.5614C11.2418 14.1014 10.2109 14.4205 9 14.4205C6.65591 14.4205 4.67182 12.8373 3.96409 10.71H0.957273V13.0418C2.43818 15.9832 5.48182 18 9 18Z'
                  fill='#34A853'
                />
                <path
                  d='M3.96409 10.71C3.78409 10.17 3.68182 9.59318 3.68182 9C3.68182 8.40682 3.78409 7.83 3.96409 7.29V4.95818H0.957273C0.347727 6.17318 0 7.54773 0 9C0 10.4523 0.347727 11.8268 0.957273 13.0418L3.96409 10.71Z'
                  fill='#FBBC05'
                />
                <path
                  d='M9 3.57955C10.3214 3.57955 11.5077 4.03364 12.4405 4.92545L15.0218 2.34409C13.4632 0.891818 11.4259 0 9 0C5.48182 0 2.43818 2.01682 0.957273 4.95818L3.96409 7.29C4.67182 5.16273 6.65591 3.57955 9 3.57955Z'
                  fill='#EA4335'
                />
              </svg>
            </span>
            Sign in with Google
          </button>

          {/* 네이버 로그인 버튼 */}
          <Button
            onClick={handleNaverSignIn}
            className='w-full py-6 text-base font-medium bg-[#03C75A] hover:bg-[#03C75A]/90 text-white rounded-md flex items-center justify-center'
          >
            <span className='mr-2'>
              <svg
                width='20'
                height='20'
                viewBox='0 0 20 20'
                fill='none'
                xmlns='http://www.w3.org/2000/svg'
              >
                <path
                  d='M13.5 3H6.5C4.567 3 3 4.567 3 6.5V13.5C3 15.433 4.567 17 6.5 17H13.5C15.433 17 17 15.433 17 13.5V6.5C17 4.567 15.433 3 13.5 3Z'
                  fill='white'
                />
                <path
                  d='M11.398 10.5001L8.60204 6.3501H6.3501V13.6501H8.6011V9.50009L11.3971 13.6501H13.6491V6.3501H11.398V10.5001Z'
                  fill='#03C75A'
                />
              </svg>
            </span>
            네이버 로그인
          </Button>
        </div>
      </div>
    </div>
  );
}

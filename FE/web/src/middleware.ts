import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  const accessToken = request.cookies.get('accessToken');
  const isAuthenticated = !!accessToken?.value;
  const isLoginPage = request.nextUrl.pathname === '/login';
  const isCallbackPage = request.nextUrl.pathname === '/callback';

  // 응답 생성
  const response = NextResponse.next();

  // 보안 헤더 설정
  response.headers.set('X-Frame-Options', 'DENY');
  response.headers.set('X-Content-Type-Options', 'nosniff');
  response.headers.set('Referrer-Policy', 'strict-origin-when-cross-origin');
  // response.headers.set(
  //   'Content-Security-Policy',
  //   "default-src 'self'; connect-src 'self' https://j12e106.p.ssafy.io https://kauth.kakao.com; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://dapi.kakao.com; style-src 'self' 'unsafe-inline';"
  // );

  // 로그인하지 않은 상태에서 보호된 페이지 접근 시도
  if (!isAuthenticated && !isLoginPage && !isCallbackPage) {
    return NextResponse.redirect(new URL('/login', request.url));
  }

  // 이미 로그인한 상태에서 로그인 페이지 접근 시도
  if (isAuthenticated && isLoginPage) {
    return NextResponse.redirect(new URL('/', request.url));
  }

  return response;
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
};

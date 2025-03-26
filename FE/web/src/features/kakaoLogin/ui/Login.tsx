"use client";

import { signIn, signOut, useSession } from "next-auth/react";
import Link from "next/link";

export default function Login() {
  const { data: session } = useSession();
  console.log(session)
  return (
    <>
      <div>
        <h1>Next-Auth Login</h1>
        <h2>지금 가입하세요.</h2>
        <Link href="/signup">계정 만들기</Link>
        {session && session.user ? (
          // 로그인 되어있을 경우
          <>
            <h3>{session.user.name}님 안녕하세요!</h3>
            <button onClick={() => signOut()}>
              {session.user.name}님 LogOut
            </button>
          </>
        ) : (
          <>
            <h3>이미 가입하셨나요?</h3>
            <button onClick={() => signIn()}>
              로그인하기
            </button>
          </>
        )}
      </div>
    </>
  );
}

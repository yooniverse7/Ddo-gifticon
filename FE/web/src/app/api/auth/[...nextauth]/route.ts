import NextAuth from 'next-auth/next';
import KakaoProvider from 'next-auth/providers/kakao';

const handler = NextAuth({
  providers: [
    KakaoProvider({
      clientId: process.env.KAKAO_CLIENT_ID!,
      clientSecret: process.env.KAKAO_CLIENT_SECRET!,
    }),
  ],
  callbacks: {
    async jwt({ token, account }) {
      // 카카오 인증 시 account 객체에서 access_token 추출
      if (account?.provider === 'kakao' && account.access_token) {
        token.accessToken = account.access_token;
      }
      return token;
    },

    async session({ session, token }) {
      console.log('$$$ token: ', token);
      session.accessToken = token.accessToken as string;
      console.log('$$$ session: ', session);
      return session;
    },
    // TODO: 여기서 signin을 통해 백엔드 트리거를 보냅니다.
  },
});

export { handler as GET, handler as POST };

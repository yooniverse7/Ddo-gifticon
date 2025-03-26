import NextAuth from 'next-auth/next'
import CredentialsProvider from 'next-auth/providers/credentials'
import KakaoProvider from "next-auth/providers/kakao";

const handler = NextAuth({
    providers: [
        KakaoProvider({
            clientId: process.env.KAKAO_CLIENT_ID!,
            clientSecret: process.env.KAKAO_CLIENT_SECRET!
        })
    ],
    callbacks: {
        async jwt({ token, user }) {
            return { ...token, ...user };
        },


        async session({ session, token }) {
            console.log('$$$ token: ', token)
            session.user = token as typeof session.user;
            console.log('$$$ session: ', session)
            return session;
        },
        // TODO: 여기서 signin을 통해 백엔드로 인가 코드를 보낸다.
    },
})

export { handler as GET, handler as POST }
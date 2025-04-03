import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { setAuthToken } from '@/shared/api/axiosInstance';

interface AuthState {
  accessToken: string | null;
  isAuthenticated: boolean;
  userInfo: {
    email?: string;
    nickname?: string;
    profileImage?: string;
  } | null;
  setTokens: (accessToken: string, refreshToken?: string) => void;
  setUserInfo: (info: any) => void;
  logout: () => void;
}

export const useAuthStore = create(
  persist<AuthState>(
    (set) => ({
      accessToken: null,
      isAuthenticated: false,
      userInfo: null,

      setTokens: (accessToken, refreshToken) => {
        set({ accessToken, isAuthenticated: !!accessToken });
        setAuthToken(accessToken);
      },

      setUserInfo: (info) => {
        set({ userInfo: info });
      },

      logout: () => {
        set({ accessToken: null, isAuthenticated: false, userInfo: null });
        setAuthToken(null);
        // 서버에 로그아웃 요청 보내기
        fetch('/api/auth/logout', { method: 'POST', credentials: 'include' });
      },
    }),
    {
      name: 'auth-storage',
      // 민감한 정보는 persist에서 제외
      partialize: (state: AuthState) => ({
        userInfo: state.userInfo,
        isAuthenticated: state.isAuthenticated,
        accessToken: null,
        setTokens: state.setTokens,
        setUserInfo: state.setUserInfo,
        logout: state.logout,
      }),
    }
  )
);

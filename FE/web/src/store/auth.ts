// src/store/auth.ts
import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';

// 사용자 정보 타입 정의
export interface User {
  id: number;
  nickname?: string;
  profileImage?: string;
  email?: string;
  // 백엔드에서 제공하는 사용자 정보에 맞게 조정
}

// 인증 상태 타입 정의
export interface AuthState {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;
  setToken: (token: string | null) => void;
  setUser: (user: User | null) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      user: null,
      isAuthenticated: false,

      setToken: (token) => set({
        token,
        isAuthenticated: !! token
      }),

      setUser: (user) => set({ user }),

      logout: () => set({
        token: null,
        user: null,
        isAuthenticated: false
      }),
    }),
    {
      name: 'auth-storage',
      // 브라우저 환경에서만 localStorage 접근
      storage: createJSONStorage(() => {
        if (typeof window !== 'undefined') {
          return localStorage;
        }
        return {
          getItem: () => null,
          setItem: () => null,
          removeItem: () => {}
        };
      })
    }
  )
);
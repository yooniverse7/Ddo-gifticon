'use client';

import { useState, useEffect, useCallback } from 'react';

export const handleMessageToRn = () => {
  // 웹 환경에서 실행되는 경우 더미 데이터를 사용
  if (typeof window !== 'undefined' && !window.ReactNativeWebView) {
    const dummyContacts = [
      { name: '김철수', phoneNumber: '010-1234-5678' },
      { name: '이영희', phoneNumber: '010-8765-4321' },
      { name: '박민수', phoneNumber: '010-5555-6666' },
    ];

    // 더미 데이터를 localStorage에 저장
    localStorage.setItem('dummyContacts', JSON.stringify(dummyContacts));
    return;
  }

  // React Native 환경에서는 원래 로직 실행
  if (window.ReactNativeWebView) {
    window.ReactNativeWebView.postMessage(
      JSON.stringify({
        type: 'OPEN_CONTACTS',
      })
    );
  }
};

export const useHandleContacts = () => {
  const [contacts, setContacts] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleMessageFromRn = useCallback((event: MessageEvent) => {
    if (event.source !== window) {
      try {
        const message = JSON.parse(event.data);
        if (message.type === 'CONTACTS_RESPONSE' && message.success) {
          setContacts(message.contacts);
          setIsLoading(false);
        }
      } catch (error) {
        console.error('Error parsing message from React Native:', error);
      }
    }
  }, []);

  useEffect(() => {
    document.addEventListener('message', handleMessageFromRn as EventListener);

    return () => {
      document.removeEventListener('message', handleMessageFromRn as EventListener);
    };
  }, [handleMessageFromRn]);

  return { contacts, isLoading };
};

'use client';

import React, { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { X, User, Search } from 'lucide-react';
import { useHandleContacts } from '../api/HandleContacts';

interface Contact {
  firstName?: string;
  phoneNumbers?: { value: string }[];
}

interface ContactListProps {
  setSelectedContact: React.Dispatch<
    React.SetStateAction<{
      name: string;
      phoneNumber: string;
    }>
  >;
  setIsContactListShow: React.Dispatch<React.SetStateAction<boolean>>;
}

export const ContactList = ({ setSelectedContact, setIsContactListShow }: ContactListProps) => {
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [query, setQuery] = useState<string>('');
  const { contacts: nativeContacts, isLoading } = useHandleContacts();

  useEffect(() => {
    // 웹 환경에서는 localStorage에서 더미 데이터를 가져옴
    if (typeof window !== 'undefined' && !window.ReactNativeWebView) {
      const dummyContacts = localStorage.getItem('dummyContacts');
      if (dummyContacts) {
        try {
          const parsedContacts = JSON.parse(dummyContacts);
          setContacts(
            parsedContacts.map((contact: any) => ({
              firstName: contact.name,
              phoneNumbers: [{ value: contact.phoneNumber }],
            }))
          );
        } catch (error) {
          console.error('더미 데이터 파싱 에러:', error);
        }
      } else {
        // 더미 데이터가 없는 경우 기본 더미 데이터 설정
        const defaultDummyContacts = [
          { name: '김철수', phoneNumber: '010-1234-5678' },
          { name: '이영희', phoneNumber: '010-8765-4321' },
          { name: '박민수', phoneNumber: '010-5555-6666' },
        ];
        localStorage.setItem('dummyContacts', JSON.stringify(defaultDummyContacts));
        setContacts(
          defaultDummyContacts.map((contact) => ({
            firstName: contact.name,
            phoneNumbers: [{ value: contact.phoneNumber }],
          }))
        );
      }
      return;
    }

    // React Native 환경에서는 메시지 이벤트 리스너 설정
    const handleMessage = (event: MessageEvent) => {
      try {
        const data = JSON.parse(event.data);
        if (data.type === 'CONTACTS_RESPONSE') {
          setContacts(data.contacts);
        }
      } catch (error) {
        console.error('메시지 파싱 에러:', error);
      }
    };

    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, []);

  // React Native 환경에서 contacts 상태 업데이트
  useEffect(() => {
    if (typeof window !== 'undefined' && window.ReactNativeWebView && nativeContacts) {
      setContacts(nativeContacts);
    }
  }, [nativeContacts]);

  const sortedContacts = [...contacts].sort((a, b) => {
    const nameA = a.firstName ? a.firstName : '';
    const nameB = b.firstName ? b.firstName : '';

    // 한글이 영어보다 먼저 오도록 정렬 로직 변경
    const testerA = /[ㄱ-ㅎㅏ-ㅣ가-힣]/.test(nameA);
    const testerB = /[ㄱ-ㅎㅏ-ㅣ가-힣]/.test(nameB);

    if (testerA && !testerB) {
      return -1; // a가 한글이고 b가 영문이면 a를 먼저 정렬
    } else if (!testerA && testerB) {
      return 1; // b가 한글이고 a가 영문이면 b를 먼저 정렬
    } else {
      // 둘 다 한글이거나 둘 다 영문인 경우 localeCompare로 정렬
      return nameA.localeCompare(nameB, 'ko');
    }
  });

  const handleContact = (name: string, phoneNumber: string) => {
    setSelectedContact({ name, phoneNumber });
    setIsContactListShow(false);
  };

  const filteredContacts = sortedContacts.filter((contact) => {
    const firstName = contact.firstName || ''; // firstName이 undefined일 경우 빈 문자열 할당
    return (
      typeof firstName === 'string' && // firstName이 문자열인지 확인
      firstName.includes(query)
    );
  });

  return (
    <div className='bg-white rounded-lg border p-6 space-y-6'>
      {/* 헤더 */}
      <div className='flex items-center justify-between'>
        <div className='flex items-center gap-2'>
          <User className='h-5 w-5 text-primary' />
          <h2 className='text-xl font-bold text-gray-900'>받는 사람 선택</h2>
        </div>
        <button
          className='p-2 hover:bg-gray-100 rounded-full transition-colors'
          onClick={() => setIsContactListShow(false)}
        >
          <X className='h-5 w-5 text-gray-500' />
        </button>
      </div>

      {/* 검색바 */}
      <div className='relative'>
        <Search className='absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400' />
        <input
          type='text'
          placeholder='이름으로 검색'
          className='w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/20'
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {/* 연락처 목록 */}
      <div className='space-y-4 max-h-[400px] overflow-y-auto'>
        {filteredContacts.length === 0 ? (
          <div className='text-center text-gray-500 py-4'>연락처가 없습니다.</div>
        ) : (
          filteredContacts.map((contact) => (
            <Button
              key={contact.phoneNumbers?.[0]?.value || 'unknown'}
              variant='ghost'
              className='w-full justify-start p-4 hover:bg-gray-50'
              onClick={() =>
                handleContact(contact.firstName || '', contact.phoneNumbers?.[0]?.value || '')
              }
            >
              <div className='flex items-center gap-3'>
                <div className='w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center'>
                  <span className='text-primary font-medium'>{contact.firstName?.[0] || '?'}</span>
                </div>
                <div className='text-left'>
                  <p className='font-medium text-gray-900'>{contact.firstName || '이름 없음'}</p>
                  <p className='text-sm text-gray-500'>
                    {contact.phoneNumbers?.[0]?.value || '전화번호 없음'}
                  </p>
                </div>
              </div>
            </Button>
          ))
        )}
      </div>
    </div>
  );
};

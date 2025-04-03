'use client';

import React, { useState } from 'react';
import { X, Lock } from 'lucide-react';
import { useRouter, useSearchParams } from 'next/navigation';
import { Button } from '@/components/ui/button';

const PinDemo: React.FC = () => {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [input, setInput] = useState<string>('');
  const from = searchParams.get('from');
  const amount = searchParams.get('amount');
  const recipient = searchParams.get('recipient');
  const storeName = searchParams.get('storeName');

  const handleKeyPress = (key: string) => {
    if (input.length < 6) {
      setInput((prevInput) => prevInput + key);
    }
  };

  const handleBackspace = () => {
    setInput((prevInput) => prevInput.slice(0, -1));
  };

  const handleClear = () => {
    setInput('');
  };

  const handleSubmit = () => {
    if (from === 'giftForm') {
      router.push(
        `/pay/completed?from=giftForm&amount=${amount}&recipient=${recipient}&storeName=${storeName}`
      );
    } else if (from === 'moneyCharge') {
      router.push(`/pay/completed?from=moneyCharge&amount=${amount}`);
    }
  };

  return (
    <div className='min-h-screen bg-gray-50'>
      {/* 헤더 */}
      <div className='fixed top-0 left-0 right-0 bg-white border-b z-10'>
        <div className='max-w-md mx-auto px-4 h-16 flex items-center justify-between'>
          <Button
            variant='ghost'
            size='icon'
            className='hover:bg-gray-100'
            onClick={() => router.back()}
          >
            <X className='h-5 w-5 text-gray-500' />
          </Button>
          <h1 className='text-lg font-semibold text-gray-900'>결제 비밀번호</h1>
          <div className='w-10' /> {/* 균형을 위한 빈 공간 */}
        </div>
      </div>

      {/* 메인 컨텐츠 */}
      <div className='pt-20 pb-8 px-4'>
        <div className='max-w-md mx-auto bg-white rounded-2xl shadow-sm p-6 space-y-8'>
          {/* 안내 메시지 */}
          <div className='text-center space-y-2'>
            <div className='flex justify-center'>
              <Lock className='h-8 w-8 text-primary' />
            </div>
            <p className='text-gray-600'>
              결제를 진행하기 위해 비밀번호를 입력해주세요
            </p>
          </div>

          {/* 비밀번호 표시 */}
          <div className='flex justify-center space-x-3'>
            {[...Array(6)].map((_, index) => (
              <div
                key={index}
                className={`w-3 h-3 rounded-full transition-all duration-200 ${
                  index < input.length ? 'bg-primary scale-125' : 'bg-gray-200'
                }`}
              />
            ))}
          </div>

          {/* 키패드 */}
          <div className='grid grid-cols-3 gap-4'>
            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 'C', 0, '←'].map((num, index) => (
              <button
                key={index}
                onClick={() => {
                  if (num === '←') {
                    handleBackspace();
                  } else if (num === 'C') {
                    handleClear();
                  } else {
                    handleKeyPress(num.toString());
                  }
                }}
                className={`aspect-square text-xl font-medium rounded-xl transition-colors ${
                  num === 'C'
                    ? 'bg-red-50 text-red-500 hover:bg-red-100'
                    : 'bg-gray-50 text-gray-700 hover:bg-gray-100'
                }`}
              >
                {num}
              </button>
            ))}
          </div>

          {/* 결제 버튼 */}
          <Button
            className='w-full py-6 text-lg font-medium bg-primary hover:bg-primary/90'
            onClick={handleSubmit}
            disabled={input.length !== 6}
          >
            결제하기
          </Button>
        </div>
      </div>
    </div>
  );
};

export default PinDemo;

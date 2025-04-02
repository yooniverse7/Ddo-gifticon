'use client';

import React from 'react';
import { CheckCircle2, Gift } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/ui/button';

export default function PayCompleted() {
  const router = useRouter();

  return (
    <div className='min-h-screen bg-gray-50'>
      {/* 메인 컨텐츠 */}
      <div className='flex flex-col items-center justify-center min-h-screen px-4'>
        <div className='max-w-md w-full bg-white rounded-2xl shadow-sm p-8 space-y-8 text-center'>
          {/* 완료 아이콘 */}
          <div className='flex justify-center'>
            <div className='w-20 h-20 rounded-full bg-primary/10 flex items-center justify-center'>
              <CheckCircle2 className='w-12 h-12 text-primary' />
            </div>
          </div>

          {/* 완료 메시지 */}
          <div className='space-y-2'>
            <h1 className='text-2xl font-bold text-gray-900'>
              결제가 완료되었습니다
            </h1>
            <p className='text-gray-600'>선물이 성공적으로 전달되었습니다</p>
          </div>

          {/* 선물 정보 요약 */}
          <div className='bg-gray-50 rounded-xl p-4 space-y-3'>
            <div className='flex items-center justify-center gap-2 text-primary'>
              <Gift className='w-5 h-5' />
              <span className='font-medium'>선물 정보</span>
            </div>
            <div className='text-sm text-gray-600 space-y-1'>
              <p>받는 사람: 홍길동</p>
              <p>선물 금액: 15,000원</p>
              <p>선물 가게: 맛있는 식당</p>
            </div>
          </div>

          {/* 버튼 그룹 */}
          <div className='space-y-3'>
            <Button
              className='w-full py-6 text-lg font-medium bg-primary hover:bg-primary/90'
              onClick={() => router.push('/gift/create')}
            >
              다른 선물하기
            </Button>
            <Button
              variant='outline'
              className='w-full py-6 text-lg font-medium'
              onClick={() => router.push('/')}
            >
              홈으로 돌아가기
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

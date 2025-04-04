'use client';

import { CheckCircle2, Gift } from 'lucide-react';
import { useRouter, useSearchParams } from 'next/navigation';
import { Button } from '@/components/ui/button';
import { Suspense } from 'react';

function CompletedPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const from = searchParams.get('from');
  const amount = searchParams.get('amount');
  const recipient = searchParams.get('recipient');
  const storeName = searchParams.get('storeName');

  return (
    <div className='min-h-screen bg-gray-50'>
      {/* 메인 컨텐츠 */}
      <div className='pt-20 pb-8 px-4'>
        <div className='max-w-md mx-auto bg-white rounded-2xl shadow-sm p-6 space-y-8'>
          {/* 완료 메시지 */}
          <div className='text-center space-y-4'>
            <div className='flex justify-center'>
              <div className='w-16 h-16 rounded-full bg-primary/10 flex items-center justify-center'>
                <CheckCircle2 className='w-10 h-10 text-primary' />
              </div>
            </div>
            <div>
              <h2 className='text-xl font-semibold text-gray-900 mb-1'>
                {from === 'moneyCharge' ? '충전 완료' : '선물하기 완료'}
              </h2>
              <p className='text-gray-600'>
                {from === 'moneyCharge' ? '충전이 완료되었습니다' : '선물하기가 완료되었습니다'}
              </p>
            </div>
          </div>

          {/* 충전/선물 정보 */}
          <div className='space-y-4'>
            <div className='flex items-center justify-between p-4 bg-gray-50 rounded-lg'>
              <span className='text-gray-600'>금액</span>
              <span className='font-semibold'>{amount}원</span>
            </div>
            {from !== 'moneyCharge' && (
              <>
                <div className='flex items-center justify-between p-4 bg-gray-50 rounded-lg'>
                  <span className='text-gray-600'>받는 사람</span>
                  <span className='font-semibold'>{recipient}</span>
                </div>
                <div className='flex items-center justify-between p-4 bg-gray-50 rounded-lg'>
                  <span className='text-gray-600'>매장</span>
                  <span className='font-semibold'>{storeName}</span>
                </div>
              </>
            )}
          </div>

          {/* 버튼 */}
          <div className='space-y-4'>
            <Button className='w-full' onClick={() => router.push('/')}>
              홈으로
            </Button>
            {from !== 'moneyCharge' && (
              <Button variant='outline' className='w-full' onClick={() => router.push('/gift/get')}>
                선물함으로
              </Button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default function Page() {
  return (
    <Suspense
      fallback={
        <div className='min-h-screen bg-gray-50 flex items-center justify-center'>
          <div className='text-center'>
            <h2 className='text-xl font-semibold text-gray-900 mb-2'>로딩 중...</h2>
            <p className='text-gray-600'>잠시만 기다려주세요.</p>
          </div>
        </div>
      }
    >
      <CompletedPageContent />
    </Suspense>
  );
}

'use client';

import { CheckCircle2, Gift } from 'lucide-react';
import { useRouter, useSearchParams } from 'next/navigation';
import { Button } from '@/components/ui/button';

export default function Page() {
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
                {from === 'moneyCharge'
                  ? '충전이 완료되었습니다'
                  : '선물하기가 완료되었습니다'}
              </p>
            </div>
          </div>

          {/* 충전/선물 정보 */}
          <div className='bg-gray-50 rounded-xl p-4 space-y-3'>
            <div className='flex items-center justify-center gap-2 text-primary'>
              <Gift className='w-5 h-5' />
              <span className='font-medium'>
                {from === 'moneyCharge' ? '충전 정보' : '선물 정보'}
              </span>
            </div>
            <div className='text-sm text-gray-600 space-y-2 text-center'>
              <p>
                {from === 'moneyCharge' ? '충전 금액' : '선물 금액'}:{' '}
                {amount ? parseInt(amount).toLocaleString('ko-KR') : '0'}원
              </p>
              {from === 'giftForm' && (
                <>
                  <p>받는 사람: {recipient}</p>
                  <p>선물 가게: {storeName}</p>
                </>
              )}
            </div>
          </div>

          {/* 버튼 */}
          <div className='space-y-3 pt-4'>
            {from === 'moneyCharge' ? (
              <Button
                className='w-full py-6 text-lg font-medium bg-primary hover:bg-primary/90'
                onClick={() => router.push('/gift/create')}
              >
                선물하러 가기
              </Button>
            ) : null}
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

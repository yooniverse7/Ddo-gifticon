'use client';
import { Button } from '@/components/ui/button';
import useFetchGiftDetail from '@/features/gitfBox/api/useFetchGiftDetail';
import { TGiftDetail } from '@/features/gitfBox/model/giftDetail';
import GivenGiftDetail from '@/features/gitfBox/ui/GivenGiftDetail';
import { X } from 'lucide-react';
import { useRouter, useSearchParams } from 'next/navigation';
import { Suspense } from 'react';

function DetailPageContent() {
  const giftId = useSearchParams().get('id');
  const { giftDetail } = useFetchGiftDetail(Number(giftId));
  const router = useRouter();

  return (
    <div className='min-h-screen bg-gray-50'>
      <div className='max-w-2xl mx-auto px-4 py-8'>
        {/* 헤더 */}
        <div className='flex items-center justify-between mb-8'>
          <Button
            variant='ghost'
            size='icon'
            className='hover:bg-gray-100 rounded-full'
            onClick={() => router.back()}
          >
            <X className='h-5 w-5' />
          </Button>
          <h1 className='text-2xl font-bold text-gray-900'>선물 상세</h1>
          <div className='w-10' /> {/* 균형을 위한 빈 공간 */}
        </div>

        {/* 선물 상세 정보 */}
        <div className='bg-white rounded-2xl shadow-sm p-6'>
          <GivenGiftDetail giftDetail={giftDetail ?? ({} as TGiftDetail)} />
        </div>
      </div>
    </div>
  );
}

export default function DetailPage() {
  return (
    <Suspense
      fallback={
        <div className='min-h-screen bg-gray-50 flex items-center justify-center'>
          <div className='text-center'>
            <h2 className='text-xl font-semibold text-gray-900 mb-2'>
              로딩 중...
            </h2>
            <p className='text-gray-600'>잠시만 기다려주세요.</p>
          </div>
        </div>
      }
    >
      <DetailPageContent />
    </Suspense>
  );
}

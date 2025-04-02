'use client';

import { Button } from '@/components/ui/button';
import { GiftForm } from '@/features/giftForm';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';
import { X } from 'lucide-react';
import { useRouter } from 'next/navigation';

export default function page() {
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
          <h1 className='text-2xl font-bold text-gray-900'>선물하기</h1>
          <div className='w-10' /> {/* 균형을 위한 빈 공간 */}
        </div>

        {/* 선물 폼 */}
        <FadeUpContainer className='bg-white rounded-2xl shadow-sm p-6'>
          <GiftForm />
        </FadeUpContainer>
      </div>
    </div>
  );
}

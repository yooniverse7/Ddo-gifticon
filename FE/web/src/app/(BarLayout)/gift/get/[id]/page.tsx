'use client';
import { Button } from '@/components/ui/button';
import GivenGiftDetail from '@/features/gitfBox/ui/GivenGiftDetail';
import { useSearchParams } from 'next/navigation';
import { X } from 'lucide-react';
import { useRouter } from 'next/navigation';

export default function DetailPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const title = searchParams.get('title');
  const image = searchParams.get('image');
  const sendUserName = searchParams.get('send_user_name');
  const props = {
    title: title ?? '',
    image: image ?? '',
    sendUserName: sendUserName ?? '',
  };

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
          <GivenGiftDetail
            title={props.title}
            image={props.image}
            sendUserName={props.sendUserName}
          />
        </div>
      </div>
    </div>
  );
}

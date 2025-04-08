import { Card, CardFooter } from '@/components/ui/card';
import { TSentGift } from '@/entity/gift/model/gift';
import { Calendar, User } from 'lucide-react';
import Image from 'next/image';
import { formatServerDate } from '@/shared/utils/dataFormatters';
import { axiosInstance } from '@/shared/api/axiosInstance';
import { API_URL } from '@/shared/constants/url';
export default function SentGiftItem({ list: list }: { list: TSentGift[] }) {
  const handleRefund = (giftId: number) => {
    // 확인 창 띄우기
    if (window.confirm('정말 취소하겠습니까?')) {
      // 사용자가 "OK"를 눌렀을 경우에만 요청 실행
      axiosInstance
        .post(API_URL.refund, { giftId })
        .then((response) => {
          console.log('취소 성공:', response.data);
          window.location.reload();
        })
        .catch((error) => {
          console.error('취소 실패:', error.response?.data || error.message);
        });
    }
  };

  return (
    <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
      {list.map((gift) => (
        <Card
          key={gift.id}
          className='overflow-hidden border-0 shadow-sm hover:shadow-md transition-shadow'
        >
          <div className='relative aspect-square'>
            <Image
              src={gift.image}
              alt={gift.title}
              fill
              className='object-cover'
              sizes='(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw'
            />
            <div className='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent' />
            <div className='absolute bottom-4 left-4 right-4 text-white'>
              <h3 className='text-lg font-semibold line-clamp-2'>
                {gift.title}
              </h3>
              <div className='flex items-center gap-2 mt-2 text-sm'>
                <User className='h-4 w-4' />
                {/* <span className='line-clamp-1'>{gift.send_user_name}</span> */}
              </div>
            </div>
          </div>
          <CardFooter className='flex items-center justify-between p-4 bg-gray-50'>
            <div className='flex items-center gap-2 text-sm text-gray-600'>
              <Calendar className='h-4 w-4' />
              <span>~{formatServerDate(gift.expiration_date)}</span>
            </div>
            {gift.used_status === 'BEFORE_USE' && (
              <div className='text-xs px-2 py-1 rounded-full text-primary'>
                <button
                  onClick={() => handleRefund(gift.id)}
                  className='px-4 py-2 rounded bg-red-500 text-white hover:bg-red-600'
                >
                  취소하기
                </button>
              </div>
            )}
          </CardFooter>
        </Card>
      ))}
    </div>
  );
}

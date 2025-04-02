import { Card, CardFooter } from '@/components/ui/card';
import { TGift } from '@/entity/gift/model/gift';
import Link from 'next/link';
import Image from 'next/image';
import { Calendar, User } from 'lucide-react';

const GivenGifiItem = ({ list }: { list: TGift[] }) => {
  return (
    <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
      {list.map((gift) => (
        <Link
          key={gift.id}
          href={{
            pathname: `/gift/get/${gift.id}`,
            query: {
              title: gift.title,
              image: gift.image,
              send_user_name: gift.send_user_name,
            },
          }}
          className='block transition-transform hover:scale-[1.02]'
        >
          <Card className='overflow-hidden border-0 shadow-sm hover:shadow-md transition-shadow'>
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
                  <span className='line-clamp-1'>{gift.send_user_name}</span>
                </div>
              </div>
            </div>
            <CardFooter className='flex items-center justify-between p-4 bg-gray-50'>
              <div className='flex items-center gap-2 text-sm text-gray-600'>
                <Calendar className='h-4 w-4' />
                <span>~{gift.expiration_date}</span>
              </div>
              <div className='text-xs px-2 py-1 rounded-full bg-primary/10 text-primary'>
                {gift.used_status === 'BEFORE_USE' && '사용 가능'}
                {gift.used_status === 'AFTER_USE' && '사용 완료'}
                {gift.used_status === 'EXPIRED' && '만료됨'}
                {gift.used_status === 'CANCLE' && '취소 가능'}
              </div>
            </CardFooter>
          </Card>
        </Link>
      ))}
    </div>
  );
};

export default GivenGifiItem;

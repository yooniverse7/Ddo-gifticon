import { Button } from '@/components/ui/button';
import Modal from '@/shared/modal/Modal';
import { useState } from 'react';
import Image from 'next/image';
import { Gift, User, Tag } from 'lucide-react';

const GivenGiftDetail = (props: {
  image?: string;
  title?: string;
  sendUserName?: string;
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className='space-y-6'>
      {/* 선물 이미지 */}
      <div className='relative aspect-square rounded-xl overflow-hidden'>
        <Image
          src={props.image || ''}
          alt={props.title || ''}
          fill
          className='object-cover'
        />
        <div className='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent' />
      </div>

      {/* 선물 정보 */}
      <div className='space-y-4'>
        <div className='flex items-center gap-2 text-gray-600'>
          <User className='h-5 w-5' />
          <span className='text-lg'>{props.sendUserName}님의 선물입니다.</span>
        </div>

        <div className='flex items-center gap-2 text-gray-900'>
          <Gift className='h-5 w-5' />
          <h2 className='text-xl font-semibold'>{props.title}</h2>
        </div>

        {/* NFC 태그 버튼 */}
        <div className='pt-4'>
          <Button
            onClick={openModal}
            className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90'
          >
            <Tag className='h-5 w-5' />
            NFC 태그하기
          </Button>
        </div>
      </div>

      {/* 모달 */}
      <Modal isModalOpen={isModalOpen} closeModal={closeModal} />
    </div>
  );
};

export default GivenGiftDetail;

'use client';
import { Button } from '@/components/ui/button';
import Modal from '@/shared/modal/Modal';
import { useState } from 'react';
import Image from 'next/image';
import { Gift, User, Tag } from 'lucide-react';
import { useSendValidateGift } from '@/entity/gift/api/useSendValidateGift';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { BASE_URL } from '@/shared/constants/url';
import { TGiftDetail } from '../model/giftDetail';

const GivenGiftDetail = (props: {
  sendRequest?: () => void;
  giftDetail: TGiftDetail;
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { sendValidateGift } = useSendValidateGift();

  const giftDetail = props.giftDetail;
  const sendRequest = async () => {
    sendValidateGift(giftDetail.id || 0);

    const eventSource = new EventSourcePolyfill(
      `${BASE_URL}/api/sse/subscribe`,
      {
        headers: {
          'xx-auth': 'acc-tkn',
        },
      }
    );

    eventSource.addEventListener('connect', (event: any) => {
      // const data = JSON.parse(event.data);
      console.log('🟢 SSE 연결 완료 메시지:', event.data);
    });

    eventSource.addEventListener('payment-success', (event: any) => {
      const data = JSON.parse(event.data);
      console.log('🟢 결제 성공 메시지:', data);
      eventSource.close();
      closeModal();
    });

    eventSource.onerror = (error: Event) => {
      console.error('SSE Error:', error);
      eventSource.close();
    };
  };

  const openModal = () => {
    setIsModalOpen(true);
    sendRequest();
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className='space-y-6'>
      {/* 선물 이미지 */}
      <div className='relative aspect-square rounded-xl overflow-hidden'>
        <Image
          src={giftDetail?.image || ''}
          alt={giftDetail?.title || ''}
          fill
          className='object-cover'
        />
        <div className='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent' />
      </div>

      {/* 선물 정보 */}
      <div className='space-y-4'>
        <div className='flex items-center gap-2 text-gray-600'>
          <User className='h-5 w-5' />
          <span className='text-lg'>
            {/* FixME: BE로부터 누가 보낸건지 받아와서 수정해야 합니다. */}
            {giftDetail.send_user_name || '알 수 없는 사용자'}님의 선물입니다.
          </span>
        </div>

        <div className='flex items-center gap-2 text-gray-900'>
          <Gift className='h-5 w-5' />
          <h2 className='text-xl font-semibold'>{giftDetail?.message}</h2>
        </div>

        {/* NFC 태그 버튼 */}
        {giftDetail?.used_status === 'BEFORE_USE' && (
          <div className='pt-4'>
            <Button
              onClick={openModal}
              className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90'
            >
              <Tag className='h-5 w-5' />
              NFC 태그하기
            </Button>
          </div>
        )}
      </div>
      {/* 모달 */}
      <Modal isModalOpen={isModalOpen} closeModal={closeModal} />
    </div>
  );
};

export default GivenGiftDetail;

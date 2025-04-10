'use client';

import { Button } from '@/components/ui/button';
import Modal from '@/shared/modal/Modal';
import { useState } from 'react';
import Image from 'next/image';
import { Gift, User, Tag, X, QrCode } from 'lucide-react';
import {
  PaymentType,
  useSendValidateGift,
} from '@/entity/gift/api/useSendValidateGift';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { BASE_URL } from '@/shared/constants/url';
import { TGiftDetail } from '../model/giftDetail';
import QRCode from 'react-qr-code';
import { useRouter } from 'next/navigation';

type GivenGiftDetailProps = {
  sendRequest?: () => void;
  giftDetail: TGiftDetail;
};

const GivenGiftDetail = ({ giftDetail }: GivenGiftDetailProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [paymentType, setPaymentType] = useState<PaymentType | null>(null);
  const router = useRouter();

  const { sendValidateGift, paymentToken } = useSendValidateGift();

  const sendRequest = async (type: PaymentType) => {
    sendValidateGift({ giftId: giftDetail.id || 0, type });

    const eventSource = new EventSourcePolyfill(
      `${BASE_URL}/api/sse/subscribe`,
      {
        headers: {
          'xx-auth': 'acc-tkn',
        },
      }
    );

    eventSource.addEventListener('connect', (event: any) => {
      console.log('🟢 SSE 연결 완료 메시지:', event.data);
    });

    eventSource.addEventListener('payment-success', (event: any) => {
      const data = JSON.parse(event.data);
      console.log('🟢 결제 성공 메시지:', data);
      eventSource.close();
      closeModal();
      alert('결제가 성공적으로 완료되었습니다!');
      router.push(`/gift/get`);
    });

    eventSource.onerror = (error: Event) => {
      console.error('SSE Error:', error);
      eventSource.close();
    };
  };

  const openModal = (type: PaymentType) => {
    setPaymentType(type);
    setIsModalOpen(true);
    sendRequest(type);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setPaymentType(null);
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
            {giftDetail.send_user_name || '알 수 없는 사용자'}님의 선물입니다.
          </span>
        </div>

        <div className='flex items-center gap-2 text-gray-900'>
          <Gift className='h-5 w-5' />
          <h2 className='text-xl font-normal'>{giftDetail?.message}</h2>
        </div>
        <p>
          총 가격:
          {giftDetail?.amount?.toLocaleString('ko-KR', {
            maximumFractionDigits: 0,
          }) || '알 수 없음'}
        </p>

        {/* 결제 버튼 */}
        {giftDetail?.used_status === 'BEFORE_USE' && (
          <>
            <div className='pt-4'>
              <Button
                onClick={() => openModal('QR')}
                className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90'
              >
                <QrCode className='h-5 w-5' />
                QR 결제
              </Button>
            </div>
            <div>
              <Button
                onClick={() => openModal('NFC')}
                className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90 text-[#FBBC05]'
              >
                <Tag className='h-5 w-5' />
                NFC 태그하기
              </Button>
            </div>
          </>
        )}
      </div>
      {/* 모달 */}
      <Modal isModalOpen={isModalOpen} closeModal={closeModal}>
        <div
          className='bg-white rounded-t-2xl w-full max-w-md p-6 h-[70vh] flex flex-col'
          onClick={(e) => e.stopPropagation()} // 내부 클릭 시 이벤트 전파 방지
        >
          {/* 헤더 */}
          <div className='flex justify-between items-center mb-6'>
            <div className='flex items-center gap-2'>
              {paymentType === 'NFC' ? (
                <Tag className='h-5 w-5 text-primary' />
              ) : (
                <QrCode className='h-5 w-5 text-primary' />
              )}
              <h2 className='text-xl font-normal text-gray-900'>
                {paymentType === 'NFC' ? 'NFC 결제' : 'QR 결제'}
              </h2>
            </div>
            <button
              className='p-2 hover:bg-gray-100 rounded-full transition-colors'
              onClick={closeModal}
            >
              <X className='h-5 w-5 text-gray-500' />
            </button>
          </div>

          {/* 컨텐츠 */}
          <div className='flex-1 flex flex-col items-center justify-center space-y-6'>
            {paymentType === 'NFC' ? (
              <>
                {/* NFC 로고 */}
                <div className='relative w-48 h-48'>
                  <Image
                    src='/NFCLogo.svg'
                    alt='NFC 결제'
                    fill
                    className='object-contain'
                    priority
                  />
                </div>

                {/* 안내 메시지 */}
                <div className='text-center space-y-2'>
                  <p className='text-lg font-normal text-gray-900'>
                    결제를 위해 NFC 태그를 읽혀주세요
                  </p>
                  <p className='text-sm text-gray-500'>
                    태그를 읽으면 자동으로 결제가 진행됩니다
                  </p>
                </div>

                {/* 로딩 인디케이터 */}
                <div className='flex items-center gap-2 text-primary'>
                  <div className='w-2 h-2 rounded-full bg-primary animate-bounce' />
                  <div className='w-2 h-2 rounded-full bg-primary animate-bounce [animation-delay:0.2s]' />
                  <div className='w-2 h-2 rounded-full bg-primary animate-bounce [animation-delay:0.4s]' />
                </div>
              </>
            ) : (
              <>
                {/* QR 코드 */}
                {paymentToken && (
                  <div className='p-4 bg-white rounded-lg shadow-md'>
                    <QRCode value={paymentToken} size={200} />
                  </div>
                )}

                {/* 안내 메시지 */}
                <div className='text-center space-y-2'>
                  <p className='text-lg font-medium text-gray-900'>
                    QR 코드를 스캔하여 결제를 진행해주세요
                  </p>
                  <p className='text-sm text-gray-500'>
                    QR 코드를 스캔하면 자동으로 결제가 진행됩니다
                  </p>
                </div>
              </>
            )}
          </div>

          {/* 하단 안내 */}
          <div className='mt-6 text-center'>
            <p className='text-sm text-gray-500'>
              결제를 취소하려면 모달을 닫아주세요
            </p>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default GivenGiftDetail;

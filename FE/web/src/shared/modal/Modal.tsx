import React from 'react';
import { X, Tag } from 'lucide-react';
import Image from 'next/image';

interface ModalProps {
  isModalOpen: boolean; // 모달 열림 여부
  closeModal: () => void; // 모달 닫기 함수
}

const Modal: React.FC<ModalProps> = ({ isModalOpen, closeModal }) => {
  if (!isModalOpen) return null; // 모달이 열리지 않았으면 렌더링하지 않음

  return (
    <div
      className='fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-end z-50'
      onClick={closeModal}
    >
      <div
        className='bg-white rounded-t-2xl w-full max-w-md p-6 h-[70vh] flex flex-col'
        onClick={(e) => e.stopPropagation()} // 내부 클릭 시 이벤트 전파 방지
      >
        {/* 헤더 */}
        <div className='flex justify-between items-center mb-6'>
          <div className='flex items-center gap-2'>
            <Tag className='h-5 w-5 text-primary' />
            <h2 className='text-xl font-bold text-gray-900'>NFC 결제</h2>
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
            <p className='text-lg font-medium text-gray-900'>
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
        </div>

        {/* 하단 안내 */}
        <div className='mt-6 text-center'>
          <p className='text-sm text-gray-500'>
            결제를 취소하려면 모달을 닫아주세요
          </p>
        </div>
      </div>
    </div>
  );
};

export default Modal;

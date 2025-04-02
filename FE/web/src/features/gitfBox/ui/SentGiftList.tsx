'use client';

import { useEffect, useState } from 'react';
import GivenGifiItem from './GivenGiftItem';
import { useFetchGift } from '@/entity/gift/api/useFetchGift';
import { TGift } from '@/entity/gift/model/gift';
import { Button } from '@/components/ui/button';
import { Send, RefreshCw, CheckCircle2 } from 'lucide-react';

const SentGiftList = () => {
  const [giftNumber, setGiftNumber] = useState(0);
  const [currentList, setCurrentList] = useState<TGift[]>([]);
  const [activeTab, setActiveTab] = useState<'refundable' | 'nonRefundable'>(
    'refundable'
  );

  // FIX ME: 보낸 선물 목록을 받아오는 useFetchSentGift 훅을 만들어야 함.
  const giftList = useFetchGift();

  const refundableList: TGift[] = [];
  const nonRefundableList: TGift[] = [];

  giftList.gifts.forEach((gift) => {
    if (gift.used_status === 'CANCLE') {
      refundableList.push(gift);
    } else {
      nonRefundableList.push(gift);
    }
  });

  useEffect(() => {
    setGiftNumber(giftList.gifts.length);
    setCurrentList(refundableList);
  }, [giftList.gifts.length]);

  const handleListChange = (
    newList: TGift[],
    tab: 'refundable' | 'nonRefundable'
  ) => {
    setCurrentList(newList);
    setActiveTab(tab);
  };

  return (
    <div className='space-y-6'>
      {/* 선물 개수 표시 */}
      <div className='text-center space-y-1'>
        <h2 className='text-xl font-semibold text-gray-900'>
          친구들에게 보낸 선물이
        </h2>
        <p className='text-3xl font-bold text-primary'>{giftNumber}개</p>
        <p className='text-gray-500'>있어요</p>
      </div>

      {/* 필터 버튼 */}
      <div className='flex justify-center gap-2'>
        <Button
          variant={activeTab === 'refundable' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'refundable'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(refundableList, 'refundable')}
        >
          <RefreshCw className='h-4 w-4' />
          취소 가능
        </Button>
        <Button
          variant={activeTab === 'nonRefundable' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'nonRefundable'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(nonRefundableList, 'nonRefundable')}
        >
          <CheckCircle2 className='h-4 w-4' />
          취소 불가
        </Button>
      </div>

      {/* 선물 목록 */}
      {currentList.length > 0 ? (
        <GivenGifiItem list={currentList} />
      ) : (
        <div className='text-center py-8 text-gray-500'>
          {activeTab === 'refundable' && '취소 가능한 선물이 없습니다.'}
          {activeTab === 'nonRefundable' && '취소 불가능한 선물이 없습니다.'}
        </div>
      )}
    </div>
  );
};

export default SentGiftList;

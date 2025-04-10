'use client';

import { useEffect, useState } from 'react';

import { TSentGift } from '@/entity/gift/model/gift';
import { Button } from '@/components/ui/button';
import { Send, RefreshCw, CheckCircle2 } from 'lucide-react';
import useFetchSentGift from '../api/useFetchSentGift';
import SentGiftItem from './SentGiftItem';

const SentGiftList = () => {
  const [giftNumber, setGiftNumber] = useState(0);
  const [currentList, setCurrentList] = useState<TSentGift[]>([]);
  const [activeTab, setActiveTab] = useState<'cancelable' | 'nonCancelable'>(
    'cancelable'
  );

  const sentGifts = useFetchSentGift();

  const cancelableList: TSentGift[] = [];
  const nonCancelableList: TSentGift[] = [];

  sentGifts?.forEach((gift) => {
    if (gift.used_status === 'BEFORE_USE') {
      cancelableList.push(gift);
    } else {
      nonCancelableList.push(gift);
    }
  });

  useEffect(() => {
    setGiftNumber(sentGifts?.length ?? 0);
    setCurrentList(cancelableList);
  }, [sentGifts?.length]);

  const handleListChange = (
    newList: TSentGift[],
    tab: 'cancelable' | 'nonCancelable'
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
          variant={activeTab === 'cancelable' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'cancelable'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(cancelableList, 'cancelable')}
        >
          <RefreshCw className='h-4 w-4' />
          취소 가능
        </Button>
        <Button
          variant={activeTab === 'nonCancelable' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'nonCancelable'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(nonCancelableList, 'nonCancelable')}
        >
          <CheckCircle2 className='h-4 w-4' />
          취소 불가
        </Button>
      </div>

      {/* 선물 목록 */}
      {currentList.length > 0 ? (
        <SentGiftItem list={currentList} />
      ) : (
        <div className='text-center py-8 text-gray-500'>
          {activeTab === 'cancelable' && '취소 가능한 선물이 없습니다.'}
          {activeTab === 'nonCancelable' && '취소 불가능한 선물이 없습니다.'}
        </div>
      )}
    </div>
  );
};

export default SentGiftList;

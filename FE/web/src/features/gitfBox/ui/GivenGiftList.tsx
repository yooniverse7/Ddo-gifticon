'use client';

import { useEffect, useState } from 'react';
import GivenGifiItem from './GivenGiftItem';
import { useFetchGift } from '@/entity/gift/api/useFetchGift';
import { TGift } from '@/entity/gift/model/gift';
import { Button } from '@/components/ui/button';
import { Gift, CheckCircle2, Clock } from 'lucide-react';

const GivenGiftList = () => {
  const [giftNumber, setGiftNumber] = useState(0);
  const [currentList, setCurrentList] = useState<TGift[]>([]);
  const [activeTab, setActiveTab] = useState<'usable' | 'afterUse' | 'expired'>(
    'usable'
  );

  const { gifts } = useFetchGift();

  const usableList: TGift[] = [];
  const afterUseList: TGift[] = [];
  const expiredList: TGift[] = [];

  gifts.forEach((gift) => {
    switch (gift.used_status) {
      case 'BEFORE_USE':
        usableList.push(gift);
        break;
      case 'AFTER_USE':
        afterUseList.push(gift);
        break;
      case 'EXPIRED':
        expiredList.push(gift);
        break;
      default:
        break;
    }
  });

  useEffect(() => {
    setGiftNumber(usableList.length);
    setCurrentList(usableList);
  }, [usableList.length]);

  const handleListChange = (
    newList: TGift[],
    tab: 'usable' | 'afterUse' | 'expired'
  ) => {
    setCurrentList(newList);
    setActiveTab(tab);
  };

  return (
    <div className='space-y-6'>
      {/* 선물 개수 표시 */}
      <div className='text-center space-y-1'>
        <h2 className='text-xl font-semibold text-gray-900'>
          사용 가능한 선물이
        </h2>
        <p className='text-3xl font-bold text-primary'>{giftNumber}개</p>
        <p className='text-gray-500'>남아있어요</p>
      </div>

      {/* 필터 버튼 */}
      <div className='flex justify-center gap-2'>
        <Button
          variant={activeTab === 'usable' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'usable'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(usableList, 'usable')}
        >
          <Gift className='h-4 w-4' />
          사용 가능
        </Button>
        <Button
          variant={activeTab === 'afterUse' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'afterUse'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(afterUseList, 'afterUse')}
        >
          <CheckCircle2 className='h-4 w-4' />
          사용 완료
        </Button>
        <Button
          variant={activeTab === 'expired' ? 'default' : 'outline'}
          className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
            activeTab === 'expired'
              ? 'bg-primary text-white shadow-sm'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
          onClick={() => handleListChange(expiredList, 'expired')}
        >
          <Clock className='h-4 w-4' />
          만료된 선물
        </Button>
      </div>

      {/* 선물 목록 */}
      {currentList.length > 0 ? (
        <GivenGifiItem list={currentList} />
      ) : (
        <div className='text-center py-8 text-gray-500'>
          {activeTab === 'usable' && '사용 가능한 선물이 없습니다.'}
          {activeTab === 'afterUse' && '사용 완료된 선물이 없습니다.'}
          {activeTab === 'expired' && '만료된 선물이 없습니다.'}
        </div>
      )}
    </div>
  );
};

export default GivenGiftList;

'use client';

import { useState } from 'react';
import GivenGifiItem from './GivenGiftItem';
import { UsedStatus } from '@/entity/gift/model/gift';
import { Button } from '@/components/ui/button';
import { Gift, CheckCircle2, Clock } from 'lucide-react';
import { useFetchGift } from '@/entity/gift/api/useFetchGift';

const GivenGiftList = () => {
  const [activeTab, setActiveTab] = useState<UsedStatus>('BEFORE_USE');
  const { gifts } = useFetchGift();
  const filteredGifts = gifts.filter((gift) => gift.used_status === activeTab);

  return (
    <div className='space-y-6'>
      {/* 선물 개수 표시 */}
      <div className='text-center space-y-1'>
        <h2 className='text-xl font-normal text-gray-900'>
          {activeTab === 'BEFORE_USE'
            ? '사용 가능한 선물이'
            : activeTab === 'AFTER_USE'
            ? '사용 완료한 선물이'
            : '만료된 선물이'}
        </h2>
        <p className='text-3xl font-normal text-[#FBBC05] '>
          {filteredGifts.length} <span className='text-lg font-medium'>개</span>
        </p>
      </div>

      {/* 필터 버튼 */}
      <div className='flex justify-center gap-1'>
        {[
          { status: 'BEFORE_USE', label: '사용 가능', icon: Gift },
          { status: 'AFTER_USE', label: '사용 완료', icon: CheckCircle2 },
          { status: 'EXPIRED', label: '만료된 선물', icon: Clock },
        ].map(({ status, label, icon: Icon }) => (
          <Button
            key={status}
            variant={activeTab === status ? 'default' : 'outline'}
            className={`flex items-center gap-2 px-4 py-2 rounded-full transition-all ${
              activeTab === status
                ? 'bg-[#FBBC05] text-white shadow-sm'
                : 'bg-white text-gray-700 hover:bg-gray-50'
            }`}
            onClick={() => setActiveTab(status as UsedStatus)}
          >
            <Icon className='h-4 w-4' />
            {label}
          </Button>
        ))}
      </div>

      {/* 선물 목록 */}
      <GivenGifiItem list={filteredGifts} />
    </div>
  );
};

export default GivenGiftList;

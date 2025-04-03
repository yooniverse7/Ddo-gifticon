'use client';

import { ArrowLeft } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { useState } from 'react';

export default function Page() {
  const router = useRouter();
  const [amount, setAmount] = useState('');

  const handleNumberClick = (num: number) => {
    if (amount.length < 8) {
      // 최대 8자리로 제한
      setAmount((prev) => prev + num);
    }
  };

  const handleDelete = () => {
    setAmount((prev) => prev.slice(0, -1));
  };

  const handleClear = () => {
    setAmount('');
  };

  const handleCharge = () => {
    if (amount) {
      router.push(`/pay/password?from=moneyCharge&amount=${amount}`);
    }
  };

  const predefinedAmounts = [1000, 5000, 10000, 50000];

  return (
    <div className='flex flex-col h-full bg-white'>
      <div className='flex items-center p-4 border-b'>
        <button onClick={() => router.back()} className='p-2'>
          <ArrowLeft className='w-6 h-6' />
        </button>
        <h1 className='flex-1 text-center text-lg font-semibold mr-8'>충전</h1>
      </div>

      <div className='p-4 flex flex-col gap-4'>
        <div className='border rounded-lg p-4'>
          <div className='text-sm text-gray-500 mb-2'>충전 금액</div>
          <div className='text-2xl font-bold'>
            {amount ? parseInt(amount).toLocaleString() : '0'}원
          </div>
        </div>

        <div className='text-sm text-gray-500'>충전 금액</div>

        <div className='flex gap-2 overflow-x-auto'>
          {predefinedAmounts.map((amt) => (
            <button
              key={amt}
              onClick={() => setAmount(amt.toString())}
              className='flex-shrink-0 px-4 py-2 rounded-full bg-gray-100 text-sm'
            >
              +{amt.toLocaleString()}원
            </button>
          ))}
        </div>
      </div>

      <div className='flex-1' />

      <div className='px-6 pb-6'>
        <div className='grid grid-cols-3 gap-2 mb-4 max-w-[350px] mx-auto'>
          {[1, 2, 3, 4, 5, 6, 7, 8, 9].map((num) => (
            <button
              key={num}
              onClick={() => handleNumberClick(num)}
              className='h-20 text-xl font-medium rounded-xl bg-gray-50 hover:bg-gray-100 border'
            >
              {num}
            </button>
          ))}
          <button
            onClick={handleClear}
            className='h-20 text-xl font-medium rounded-xl bg-gray-50 hover:bg-gray-100 border text-gray-600'
          >
            C
          </button>
          <button
            onClick={() => handleNumberClick(0)}
            className='h-20 text-xl font-medium rounded-xl bg-gray-50 hover:bg-gray-100 border'
          >
            0
          </button>
          <button
            onClick={handleDelete}
            className='h-20 text-xl font-medium rounded-xl bg-gray-50 hover:bg-gray-100 border'
          >
            ←
          </button>
        </div>

        <button
          onClick={handleCharge}
          disabled={!amount}
          className={`w-full max-w-[280px] mx-auto py-3.5 rounded-lg text-white font-medium ${
            amount ? 'bg-yellow-400' : 'bg-gray-200'
          } block`}
        >
          충전하기
        </button>
      </div>
    </div>
  );
}

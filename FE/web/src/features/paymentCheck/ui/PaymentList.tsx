'use client';

import { TPayment } from '@/entity/store/model/payment';
import { useState } from 'react';
import { PaymentIntersection } from './PaymentIntersection';

interface PaymentListProps {
  paymentList: TPayment[];
}

export const PaymentList = ({ paymentList }: PaymentListProps) => {
  // 전체 / 입금/ 출금 결제 내역을 구분해서 보여주기 위해
  // listShowState 변수를 사용한다.
  const [listShowState, setListShowState] = useState<'all' | 'in' | 'out'>(
    'all'
  );

  const filteredList =
    listShowState === 'all'
      ? paymentList
      : listShowState === 'in'
      ? paymentList.filter((payment) => payment.payment_price > 0)
      : paymentList.filter((payment) => payment.payment_price < 0);

  return (
    <div className='flex flex-col gap-4'>
      <PaymentIntersection
        listShowState={listShowState}
        setListShowState={setListShowState}
      />
      <div className='flex flex-col divide-y'>
        {filteredList.map((payment) => (
          <div
            key={payment.id}
            className='py-3 flex justify-between items-center'
          >
            <div>
              <div className='font-medium'>{payment.payment_title}</div>
              <div className='text-sm text-gray-500'>
                {new Date(payment.payment_date).toLocaleDateString()}
              </div>
            </div>
            <div
              className={
                payment.payment_price > 0 ? 'text-blue-600' : 'text-red-600'
              }
            >
              {payment.payment_price.toLocaleString()}원
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

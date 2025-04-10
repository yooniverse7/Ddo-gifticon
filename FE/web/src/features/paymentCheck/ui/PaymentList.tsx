'use client';

import { useState } from 'react';
import { PaymentIntersection } from './PaymentIntersection';
import { usePaymentList } from '../api/usePaymentList';

export const PaymentList = () => {
  const paymentList = usePaymentList();
  const [listShowState, setListShowState] = useState<'all' | 'in' | 'out'>(
    'all'
  );

  if (!paymentList) return <div>데이터가 없습니다.</div>;

  const filteredList =
    Array.isArray(paymentList) && listShowState === 'all'
      ? [...paymentList].sort(
          (a, b) => new Date(b.time).getTime() - new Date(a.time).getTime()
        )
      : Array.isArray(paymentList) && listShowState === 'in'
      ? paymentList
          .filter((payment) => payment.in_out_amount > 0)
          .sort(
            (a, b) => new Date(b.time).getTime() - new Date(a.time).getTime()
          )
      : Array.isArray(paymentList)
      ? paymentList
          .filter((payment) => payment.in_out_amount < 0)
          .sort(
            (a, b) => new Date(b.time).getTime() - new Date(a.time).getTime()
          )
      : [];
  return (
    <div className='flex flex-col gap-4'>
      <PaymentIntersection
        listShowState={listShowState}
        setListShowState={setListShowState}
      />
      <div className='space-y-4'>
        {Array.isArray(filteredList) && filteredList.length > 0 ? (
          filteredList.map((payment) => (
            <div
              key={payment.id}
              className='py-3 flex justify-between items-center'
            >
              <div>
                <div className='font-medium'>{payment.title}</div>
                <div className='text-sm text-gray-500'>
                  {new Date(payment.time).toLocaleDateString()}
                </div>
              </div>
              <div
                className={
                  payment.in_out_amount > 0 ? 'text-blue-600' : 'text-red-600'
                }
              >
                {payment.in_out_amount.toLocaleString()}원
              </div>
            </div>
          ))
        ) : (
          <div className='text-center text-gray-500 py-4'>
            결제 내역이 없습니다.
          </div>
        )}
      </div>
    </div>
  );
};

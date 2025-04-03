'use client';

import { usePaymentList } from '../api/usePaymentList';
import { PaymentList } from './PaymentList';

export const PaymentBox = () => {
  // paymentIntersection 컴포넌트
  // paymentList 컴포넌트
  // 추가될 예정.
  // 이 컴포넌트에서는 paymentIntersection 에서의 동작에 따라
  // paymentList 에 데이터를 전달하고, paymentList 에서 데이터를 받아와서 표시한다.
  const paymentList = usePaymentList();
  return (
    <div className='p-4'>
      <PaymentList paymentList={paymentList.data ?? []} />
    </div>
  );
};

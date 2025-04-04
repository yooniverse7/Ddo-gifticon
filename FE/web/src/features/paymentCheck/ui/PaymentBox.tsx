'use client';

import { usePaymentList } from '../api/usePaymentList';
import { PaymentList } from './PaymentList';

export const PaymentBox = () => {
  return (
    <div className='p-4'>
      <PaymentList />
    </div>
  );
};

import { MoneyView } from '@/features/myMoneyCheck/ui/mouneyView';
import { PaymentBox } from '@/features/paymentCheck/ui/PaymentBox';

export default function page() {
  return (
    <div className='flex flex-col h-full bg-gray-50'>
      <div className='p-4'>
        <h1 className='flex justify-center text-lg font-semibold mb-4'>
          나의 머니
        </h1>
        <MoneyView />
      </div>
      <div className='flex-1 bg-white'>
        <PaymentBox />
      </div>
    </div>
  );
}

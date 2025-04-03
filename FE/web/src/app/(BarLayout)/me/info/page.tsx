import { MoneyView } from '@/features/myMoneyCheck/ui/moneyView';
import { PaymentBox } from '@/features/paymentCheck/ui/PaymentBox';
import Link from 'next/link';

export default function page() {
  return (
    <div className='flex flex-col h-full bg-gray-50'>
      <div className='p-4'>
        <div className='relative flex justify-center items-center mb-4'>
          <h1 className='text-lg font-semibold'>나의 머니</h1>
          <div className='absolute right-0'>
            <Link href='/me/info/setting'>
              <button className='bg-white/90 text-gray-800 px-6 py-2 rounded-lg font-medium'>
                환경설정
              </button>
            </Link>
          </div>
        </div>
        <MoneyView />
      </div>
      <div className='flex-1 bg-white'>
        <PaymentBox />
      </div>
    </div>
  );
}

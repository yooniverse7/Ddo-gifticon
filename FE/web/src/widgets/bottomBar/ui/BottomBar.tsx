'use client';

import { URL } from '@/shared/constants/url';
import { Gift, Map, PlusCircle, Store, User } from 'lucide-react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';

export const BottomBar: React.FC = () => {
  const pathname = usePathname();

  return (
    <div className='fixed bottom-0 left-0 flex justify-evenly items-center w-full h-20 z-50 bg-white border-t-2'>
      <Link href={'/'}>
        <Map size={32} className={`${pathname === '/' && 'text-amber-300'}`} />
      </Link>
      <Link
        href={URL.gift_get}
        className={`${pathname.startsWith(URL.gift_get) && 'text-amber-300'}`}
      >
        <Gift size={32} />
      </Link>
      <Link
        href={URL.gift_create}
        className={`${
          pathname.startsWith(URL.gift_create) && 'text-amber-300'
        }`}
      >
        <PlusCircle size={32} />
      </Link>
      <Link
        href={URL.me_stores}
        className={`${pathname.startsWith(URL.me_stores) && 'text-amber-300'}`}
      >
        <Store size={32} />
      </Link>
      <Link
        href={URL.me_info}
        className={`${pathname.startsWith(URL.me_info) && 'text-amber-300'}`}
      >
        <User size={32} />
      </Link>
    </div>
  );
};

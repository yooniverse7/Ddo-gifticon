'use client';

import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { useFetchCrawledStore } from '@/entity/store/api/useFetchCrawledStore';
import { useSendRegisterStore } from '@/entity/store/api/useSendRegisterStore';
import { CrawledStore } from '@/features/crawledStore';
import { CustomMenuForm } from '@/features/menuForm/ui/CustomMenuForm';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';
import { X } from 'lucide-react';
import { useRouter, useSearchParams } from 'next/navigation';
import { useState } from 'react';

type Menu = {
  menu_name: string;
  menu_price: number;
};

export default function page() {
  const router = useRouter();
  const [customMenu, setCustomMenu] = useState<Menu[]>([]);
  const { mutateSendRegisterStore } = useSendRegisterStore();
  const params = useSearchParams();
  const data = params.get('data');
  const { crawledData } = useFetchCrawledStore(data);

  const addCustomMenu = (name: string, price: number) => {
    setCustomMenu((prev) => [...prev, { menu_name: name, menu_price: price }]);
  };

  const submitStore = () => {
    if (!crawledData) return;

    const formData = new FormData();

    // 기존 메뉴 정보
    const menus = crawledData.menus.map((menu) => ({
      menu_name: menu.menu_name,
      menu_price: parseInt(menu.menu_price.replace(/[^0-9]/g, '')),
      menu_image: menu.menu_image,
    }));

    // 커스텀 메뉴 정보
    const customMenus = customMenu.map((menu) => ({
      user_id: 1,
      custom_menu_name: menu.menu_name,
      custom_menu_price: menu.menu_price,
      custom_menu_image: null, // 파일 업로드 기능 추가 필요
    }));
    const json = {
      user_id: 1,
      place_name: crawledData.place_name,
      main_image_url: crawledData.main_image_url,
      address_name: crawledData.address_name,
      position: crawledData.position,
      user_intro: '직접 끓인 사골 국물이 일품!',
      star_rating: '4.5',
      visited_count: '0',
      menus,
      customMenus,
    };

    formData.append(
      'restaurantCreateRequestDto',
      new Blob([JSON.stringify(json)], { type: 'application/json' }),
    );
    mutateSendRegisterStore(formData);
  };

  return (
    <FadeUpContainer className='flex flex-col h-full items-center'>
      <div className='relative w-full bg-amber-50'>
        <Button variant={'ghost'} className='absolute top-4 left-4' onClick={() => router.back()}>
          <X className='size-6' />
        </Button>
        <h1 className='my-4 text-2xl font-bold text-center'>맛집 등록</h1>
      </div>
      {!crawledData && (
        <div className='w-full animate-pulse'>
          {/* 가게 이미지 스켈레톤 */}
          <div className='w-full h-48 bg-gray-200 rounded-lg mb-4' />

          {/* 가게 정보 스켈레톤 */}
          <div className='space-y-3 p-4'>
            <div className='h-6 bg-gray-200 rounded w-3/4' />
            <div className='h-4 bg-gray-200 rounded w-1/2' />
            <div className='h-4 bg-gray-200 rounded w-2/3' />
          </div>
        </div>
      )}
      <CrawledStore crawledData={crawledData} />
      <h2 className='font-semibold text-lg mb-2'>나만의 메뉴</h2>
      <div className='flex flex-col w-full max-h-96 px-8 overflow-y-auto'>
        <ul>
          {customMenu.map((menu) => (
            <li key={menu.menu_name}>
              <h3 className='font-bold text-xl'>{menu.menu_name}</h3>
              <p>{menu.menu_price}원</p>
              <Separator className='my-2' />
            </li>
          ))}
        </ul>
      </div>
      <h2 className='font-semibold text-lg mb-2'>나만의 메뉴 추가하기</h2>
      <CustomMenuForm addCustomMenu={addCustomMenu} />
      <Button className='fixed bottom-0 w-full h-20' onClick={submitStore}>
        등록하기
      </Button>
    </FadeUpContainer>
  );
}

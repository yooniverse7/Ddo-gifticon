'use client';

import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { CustomMenuForm } from '@/features/menuForm/ui/CustomMenuForm';
import { X } from 'lucide-react';
import { motion } from 'motion/react';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useState } from 'react';

const crawledData = {
  id: null,
  place_name: '하단끝집 하단점',
  main_image_url:
    'https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201023_276%2F1603424015305CdC4w_JPEG%2FyyDdjexjV__50xn4BHRoh5sw.jpg',
  address_name: '부산 사하구 낙동대로535번길 23 1층',
  position: {
    lat: 35.104013,
    lng: 128.966366,
  },
  store_info: '',
  menus: [
    {
      menu_name: '숯불닭다리살',
      menu_desc: '',
      menu_price: '10,000원',
      menu_image:
        'https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f320_320&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201107_258%2F1604752712809lOI5H_JPEG%2FtOQmJXWKUZSX4kXE0qQm4qb2.jpg',
    },
    {
      menu_name: '숯불무뼈닭발',
      menu_desc: '',
      menu_price: '10,000원',
      menu_image: '',
    },
    {
      menu_name: '숯불닭똥집',
      menu_desc: '',
      menu_price: '10,000원',
      menu_image:
        'https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f320_320&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201107_68%2F1604752751147jrlqr_JPEG%2F6VHdFzn_cFbYIzAu-9mH7uJK.jpg',
    },
    {
      menu_name: '날치알마요밥',
      menu_desc: '',
      menu_price: '4,500원',
      menu_image:
        'https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f320_320&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201107_80%2F1604752773299qBGMM_JPEG%2F4bVPERoWJkYAqJ43JeKKYGoA.jpg',
    },
    {
      menu_name: '심야우동',
      menu_desc: '',
      menu_price: '5,000원',
      menu_image:
        'https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f320_320&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201107_40%2F1604752783675viqBV_JPEG%2FugVOf77ROKVjHqW6fNZQvPpD.jpg',
    },
    {
      menu_name: '가마솥계란찜',
      menu_desc: '',
      menu_price: '6,000원',
      menu_image: '',
    },
  ],
};

type Menu = {
  menu_name: string;
  menu_price: number;
};

export default function page() {
  const router = useRouter();
  const [customMenu, setCustomMenu] = useState<Menu[]>([]);

  const addCustomMenu = (name: string, price: number) => {
    setCustomMenu((prev) => [...prev, { menu_name: name, menu_price: price }]);
  };

  return (
    <motion.div
      className="flex flex-col h-full items-center"
      initial={{ transform: 'translateY(100%)' }}
      animate={{ transform: 'translateY(0)' }}
    >
      <div className="relative w-full bg-amber-50">
        <Button variant={'ghost'} className="absolute top-4 left-4" onClick={() => router.back()}>
          <X className="size-6" />
        </Button>
        <h1 className="my-4 text-2xl font-bold text-center">맛집 등록</h1>
      </div>
      <div className="flex items-center w-full px-8 gap-4">
        <div className="w-40 h-40 my-4">
          <Image src={crawledData.main_image_url} width={200} height={200} alt="메인 이미지" />
        </div>
        <div>
          <h2 className="font-semibold text-xl mb-4">{crawledData.place_name}</h2>
          <p>주소 : {crawledData.address_name}</p>
        </div>
      </div>
      <h2 className="font-semibold text-lg">기존 메뉴</h2>
      <div className="flex flex-col w-full max-h-96 px-8 overflow-y-auto">
        <ul>
          {crawledData.menus.map((menu) => (
            <li key={menu.menu_name}>
              <div className="flex items-center justify-between">
                <div className="flex flex-col">
                  <h3 className="font-bold text-xl">{menu.menu_name}</h3>
                  <p>{menu.menu_price}</p>
                </div>
                {menu.menu_image !== '' && (
                  <div>
                    <Image src={menu.menu_image} width={80} height={80} alt={menu.menu_name} />
                  </div>
                )}
              </div>
              <Separator className="my-2" />
            </li>
          ))}
          {customMenu.map((menu) => (
            <li key={menu.menu_name}>
              <h3 className="font-bold text-xl">{menu.menu_name}</h3>
              <p>{menu.menu_price}원</p>
              <Separator className="my-2" />
            </li>
          ))}
        </ul>
      </div>
      <h2 className="font-semibold text-lg mb-2">나만의 메뉴 추가하기</h2>
      <CustomMenuForm addCustomMenu={addCustomMenu} />
      <Button className="fixed bottom-0 w-full h-20 ">등록하기</Button>
    </motion.div>
  );
}

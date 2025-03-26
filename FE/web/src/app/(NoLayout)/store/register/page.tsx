'use client';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Separator } from '@/components/ui/separator';
import { X } from 'lucide-react';
import { motion } from 'motion/react';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useRef, useState } from 'react';

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
  const nameRef = useRef<HTMLInputElement>(null);
  const priceRef = useRef<HTMLInputElement>(null);

  const addCustomMenu = () => {
    const name = nameRef.current?.value || '';
    const price = Number(priceRef.current?.value) || 0;

    if (!name || !price) {
      return;
    }

    setCustomMenu((prev) => [...prev, { menu_name: name, menu_price: price }]);
  };

  return (
    <motion.div
      className="flex flex-col h-full items-center"
      initial={{ transform: 'translateY(100%)' }}
      animate={{ transform: 'translateY(0)' }}
    >
      <Button className="absolute top-4 left-4" onClick={() => router.back()}>
        <X />
      </Button>
      <h1 className="text-2xl font-bold">맛집 등록</h1>
      <div className="w-40 h-40 bg-gray-200"> 대표 이미지</div>
      <h2>{crawledData.place_name}</h2>
      <p>주소 : {crawledData.address_name}</p>
      <h2>기존 메뉴</h2>
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
      <h2>나만의 메뉴 추가하기</h2>
      <div>
        <section className="flex justify-between">
          <div className="flex flex-col w-2/5">
            <Label htmlFor="menuName">메뉴 이름</Label>
            <Input
              ref={nameRef}
              type="text"
              id="menuName"
              name="menuName"
              placeholder="메뉴 이름이 뭔가요?"
            />
          </div>
          <div className="flex flex-col w-2/5">
            <Label htmlFor="menuPrice">가격</Label>
            <Input
              ref={priceRef}
              type="number"
              id="menuPrice"
              name="menuPrice"
              placeholder="가격 형성이 어떻게 되어있쬬?"
            />
          </div>
        </section>
        <Button onClick={addCustomMenu}>추가</Button>
      </div>
      <Button className="fixed bottom-0 h-20 w-full ">등록하기</Button>
    </motion.div>
  );
}

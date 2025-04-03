'use client';

import React, { useState, useEffect } from 'react';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { handleMessageToRn } from '../api/HandleContacts';
import ContactList from './ContactList';
import FavoriteMarketList from './FavoriteMarketList';
import Image from 'next/image';
import CustomMenuImageSelector from './CustomMenuImageSelector';
import Link from 'next/link';
import { User, Store, Plus, Minus, Gift } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { TMenu } from '@/entity/store/model/menu';

export const GiftForm = () => {
  const router = useRouter();
  const [isContactListShow, setIsContactListShow] = useState(false);
  const [isFavoriteMarketShow, setIsFavoriteMarketShow] = useState(false);
  const [totalPrice, setTotalPrice] = useState(0);
  const [menuList, setMenuList] = useState<TMenu[]>([]);
  const [customMenuList, setCustomMenuList] = useState<
    {
      id: number;
      custom_menu_name: string;
      custom_menu_price: string;
      custom_menu_image: string;
    }[]
  >([]);
  const [marketName, setMarketName] = useState('또갈집 찾기');
  const [selectedContact, setSelectedContact] = useState({
    name: '누구에게 보낼까요?',
    phoneNumber: '',
  });
  const [menuQuantities, setMenuQuantities] = useState<Record<number, number>>({});
  const [customMenuImages, setCustomMenuImages] = useState<{ src: string; alt: string }[]>([]);
  const [paymentData, setPaymentData] = useState<string | null>(null);

  const handleContactBtn = () => {
    handleMessageToRn();
    setIsContactListShow(true);
  };

  const handleFavoriteSearchBtn = () => {
    setIsFavoriteMarketShow(true);
    setMenuList([]);
    setCustomMenuList([]);
    setTotalPrice(0);
    setMenuQuantities({});
  };

  const increaseQuantity = (id: number): void => {
    setMenuQuantities((prevQuantities: Record<number, number>) => ({
      ...prevQuantities,
      [id]: (prevQuantities[id] || 0) + 1,
    }));
    updateTotalPrice(id, 1);
  };

  const decreaseQuantity = (id: number): void => {
    if (menuQuantities[id] > 0) {
      setMenuQuantities((prevQuantities) => ({
        ...prevQuantities,
        [id]: Math.max(0, (prevQuantities[id] || 0) - 1),
      }));
      updateTotalPrice(id, -1);
    }
  };

  const updateTotalPrice = (id: number, change: number) => {
    const menu = [...menuList, ...customMenuList].find((m) => m.id === id);
    const price =
      'menu_price' in menu!
        ? parseInt(menu.menu_price.replace(/,/g, ''))
        : parseInt(menu!.custom_menu_price.replace(/,/g, ''));
    setTotalPrice((prevTotal) => Math.max(0, prevTotal + price * change));
  };

  const handlePay = () => {
    if (selectedContact.name === '누구에게 보낼까요?') {
      alert('받는 사람을 선택해주세요.');
      return;
    }

    if (totalPrice === 0) {
      alert('선물할 메뉴를 선택해주세요.');
      return;
    }

    router.push(
      `/pay/password?from=giftForm&amount=${totalPrice}&recipient=${selectedContact.name}&storeName=${marketName}`
    );
  };

  return (
    <div className='space-y-6'>
      {/* 받는 사람 선택 */}
      <div className='space-y-2'>
        <Label className='text-sm font-medium text-gray-700'>받는 사람</Label>
        <Button
          variant='outline'
          className='w-full flex items-center justify-between p-4 h-auto'
          onClick={handleContactBtn}
        >
          <div className='flex items-center gap-2'>
            <User className='h-5 w-5 text-gray-500' />
            <span
              className={
                selectedContact.name === '누구에게 보낼까요?' ? 'text-gray-500' : 'text-gray-900'
              }
            >
              {selectedContact.name}
            </span>
          </div>
          <span className='text-sm text-gray-500'>선택하기</span>
        </Button>
      </div>
      {isContactListShow && (
        <ContactList
          setSelectedContact={setSelectedContact}
          setIsContactListShow={setIsContactListShow}
        />
      )}

      {/* 가게 선택 */}
      <div className='space-y-2'>
        <Label className='text-sm font-medium text-gray-700'>선물할 가게</Label>
        <Button
          variant='outline'
          className='w-full flex items-center justify-between p-4 h-auto'
          onClick={handleFavoriteSearchBtn}
        >
          <div className='flex items-center gap-2'>
            <Store className='h-5 w-5 text-gray-500' />
            <span className='text-gray-900'>{marketName}</span>
          </div>
          <span className='text-sm text-gray-500'>선택하기</span>
        </Button>
      </div>

      {/* 메뉴 목록 */}
      {(menuList?.length > 0 || customMenuList?.length > 0) && (
        <div className='space-y-4'>
          <Label className='text-sm font-medium text-gray-700'>선택한 메뉴</Label>
          <div className='space-y-4'>
            {[...(menuList || []), ...(customMenuList || [])].map((menu) => (
              <div
                key={menu.id}
                className='flex items-center justify-between p-4 bg-gray-50 rounded-lg'
              >
                <div className='flex items-center gap-4'>
                  <div className='relative w-16 h-16 rounded-lg overflow-hidden'>
                    <Image
                      src={'menu_image' in menu ? menu.menu_image : menu.custom_menu_image}
                      alt={'menu_name' in menu ? menu.menu_name : menu.custom_menu_name}
                      fill
                      className='object-cover'
                    />
                  </div>
                  <div>
                    <h3 className='font-medium text-gray-900'>
                      {'menu_name' in menu ? menu.menu_name : menu.custom_menu_name}
                    </h3>
                    <p className='text-sm text-gray-500'>
                      {'menu_price' in menu ? menu.menu_price : menu.custom_menu_price}
                    </p>
                  </div>
                </div>
                <div className='flex items-center gap-2'>
                  <Button
                    variant='outline'
                    size='icon'
                    className='h-8 w-8'
                    onClick={() => decreaseQuantity(menu.id)}
                  >
                    <Minus className='h-4 w-4' />
                  </Button>
                  <span className='w-8 text-center font-medium'>
                    {menuQuantities[menu.id] || 0}
                  </span>
                  <Button
                    variant='outline'
                    size='icon'
                    className='h-8 w-8'
                    onClick={() => increaseQuantity(menu.id)}
                  >
                    <Plus className='h-4 w-4' />
                  </Button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* 커스텀 메뉴 선택기 */}
      <CustomMenuImageSelector onImagesChange={setCustomMenuImages} />

      {/* 총 금액 및 결제 버튼 */}
      {totalPrice > 0 && (
        <div className='fixed bottom-0 left-0 right-0 p-4 bg-white border-t'>
          <div className='max-w-2xl mx-auto'>
            <div className='flex items-center justify-between mb-4'>
              <span className='text-lg font-medium text-gray-900'>총 금액</span>
              <span className='text-2xl font-bold text-primary'>
                {totalPrice.toLocaleString()}원
              </span>
            </div>
            {paymentData ? (
              <Link
                href={`/pay/password?data=${paymentData}`}
                className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90 text-white rounded-lg'
              >
                <Gift className='h-5 w-5' />
                선물하기
              </Link>
            ) : (
              <Button
                className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90'
                onClick={handlePay}
              >
                <Gift className='h-5 w-5' />
                선물하기
              </Button>
            )}
          </div>
        </div>
      )}

      {/* 모달 컴포넌트들 */}

      {isFavoriteMarketShow && (
        <FavoriteMarketList
          setMenuList={setMenuList}
          setCustomMenuList={setCustomMenuList}
          setIsFavoriteMarketShow={setIsFavoriteMarketShow}
          setMarketName={setMarketName}
        />
      )}
    </div>
  );
};

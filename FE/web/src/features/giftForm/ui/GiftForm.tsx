'use client';

import React, { useState, useEffect } from 'react';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { handleMessageToRn } from '../api/HandleContacts';
import { ContactList } from './ContactList';
import FavoriteMarketList from './FavoriteMarketList';
import Image from 'next/image';
import CustomMenuImageSelector from './CustomMenuImageSelector';
import { User, Store, Plus, Minus, Gift } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { TCustomMenu, TMarketResponse, TMenu } from '@/entity/store/model/menu';
import { axiosInstance } from '@/shared/api/axiosInstance';
import { Input } from '@/components/ui/input';
import { useFetchMenu } from '../api/useFetchMenu';

export const GiftForm = () => {
  const router = useRouter();
  const [isContactListShow, setIsContactListShow] = useState(false);
  const [isFavoriteMarketShow, setIsFavoriteMarketShow] = useState(false);
  const [totalPrice, setTotalPrice] = useState(0);
  const [selectedMarketId, setSelectedMarketId] = useState<number | null>(null);
  const { data: menuData } = useFetchMenu(selectedMarketId || 0, {
    enabled: !!selectedMarketId,
  });
  const [menuList, setMenuList] = useState<TMenu[]>([]);
  const [customMenuList, setCustomMenuList] = useState<TCustomMenu[]>([]);
  const [market, setMarket] = useState<TMarketResponse | null>(null);
  const [selectedContact, setSelectedContact] = useState({
    name: '누구에게 보낼까요?',
    phoneNumber: '',
  });
  const [menuQuantities, setMenuQuantities] = useState<Record<number, number>>(
    {}
  );
  const [customMenuImages, setCustomMenuImages] = useState<
    { src: string; alt: string }[]
  >([]);
  const [giftTitle, setGiftTitle] = useState('');
  const [giftMessage, setGiftMessage] = useState('');

  useEffect(() => {
    if (menuData) {
      setMenuList(menuData.menu);
      setCustomMenuList(menuData.customMenu);
    }
  }, [menuData, setMenuList, setCustomMenuList]);

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
    console.log('하이');
    if (menuQuantities[id] > 0) {
      setMenuQuantities((prevQuantities) => ({
        ...prevQuantities,
        [id]: Math.max(0, (prevQuantities[id] || 0) - 1),
      }));
      updateTotalPrice(id, -1);
    }
  };

  const updateTotalPrice = (id: number, change: number) => {
    const menu = menuList.find((m) => m.menu_id === id);
    if (!menu) return; // 메뉴를 찾지 못한 경우 함수 종료

    const price = parseInt(menu.menu_price.replace(/,/g, ''));
    setTotalPrice((prevTotal) => Math.max(0, prevTotal + price * change));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (selectedContact.name === '누구에게 보낼까요?') {
      alert('받는 사람을 선택해주세요.');
      return;
    }

    if (totalPrice === 0) {
      alert('선물할 메뉴를 선택해주세요.');
      return;
    }

    if (!giftTitle) {
      alert('선물 이름을 입력해주세요.');
      return;
    }

    try {
      const formData = new FormData();

      // 이미지 추가
      customMenuImages.forEach((image, index) => {
        // Base64 이미지를 Blob으로 변환
        const base64Data = image.src.split(',')[1];
        const byteCharacters = atob(base64Data);
        const byteArrays = [];

        for (let i = 0; i < byteCharacters.length; i++) {
          byteArrays.push(byteCharacters.charCodeAt(i));
        }

        const byteArray = new Uint8Array(byteArrays);
        const blob = new Blob([byteArray], { type: 'image/jpeg' });

        formData.append('image', blob, `custom_menu_${index}.jpg`);
      });

      // JSON 데이터 추가
      const requestData = {
        title: giftTitle,
        amount: totalPrice,
        message: giftMessage,
        phone_num: selectedContact.phoneNumber,
        res_id: market?.id,
        position: {
          lat: market?.position.lat,
          lng: market?.position.lng,
        },
      };

      formData.append('request', JSON.stringify(requestData));

      // FormData 내용 확인
      console.log('FormData 내용:');
      for (const [key, value] of formData.entries()) {
        if (key === 'request') {
          console.log(`${key}:`, JSON.parse(value as string));
        } else {
          console.log(`${key}:`, value);
        }
      }

      const response = await axiosInstance.post('/api/gift', formData);

      console.log('응답 데이터:', response.data);

      if (response.data) {
        router.push(
          `/pay/password?from=giftForm&amount=${totalPrice}&recipient=${
            selectedContact.name
          }&storeName=${market?.place_name || '선택된 가게 없음'}&giftId=${
            response.data.giftId
          }`
        );
      }
    } catch (error) {
      console.error('기프티콘 생성 실패:', error);
      alert('기프티콘 생성에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <form onSubmit={handleSubmit} className='space-y-6'>
      {/* 커스텀 메뉴 선택기 */}
      <CustomMenuImageSelector onImagesChange={setCustomMenuImages} />
      {/* 받는 사람 선택 */}
      <div className='space-y-2'>
        <Label className='text-sm font-medium text-gray-700'>받는 사람</Label>
        <Button
          variant='outline'
          className='w-full flex items-center justify-between p-4 h-auto'
          onClick={handleContactBtn}
          type='button'
        >
          <div className='flex items-center gap-2'>
            <User className='h-5 w-5 text-gray-500' />
            <span
              className={
                selectedContact.name === '누구에게 보낼까요?'
                  ? 'text-gray-500'
                  : 'text-gray-900'
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
          type='button'
        >
          <div className='flex items-center gap-2'>
            <Store className='h-5 w-5 text-gray-500' />
            <span className='text-gray-900'>
              {market?.place_name || '또갈집 찾기'}
            </span>
          </div>
          <span className='text-sm text-gray-500'>선택하기</span>
        </Button>
      </div>

      {/* 메뉴 목록 */}
      <div className='space-y-4'>
        <Label className='text-sm font-medium text-gray-700'>선택한 메뉴</Label>
        <div className='space-y-4'>
          {[...(menuList || []), ...(customMenuList || [])].map((menu) => (
            <div
              key={'menu_id' in menu ? menu.menu_id : menu.custom_menu_id}
              className='flex items-center justify-between p-4 bg-gray-50 rounded-lg'
            >
              <div className='flex items-center gap-4'>
                <div className='relative w-16 h-16 rounded-lg overflow-hidden'>
                  <Image
                    src={
                      'menu_image' in menu
                        ? menu.menu_image
                        : menu.custom_menu_image
                    }
                    alt={
                      'menu_name' in menu
                        ? menu.menu_name
                        : menu.custom_menu_name
                    }
                    fill
                    className='object-cover'
                  />
                </div>
                <div>
                  <h3 className='font-medium text-gray-900'>
                    {'menu_name' in menu
                      ? menu.menu_name
                      : menu.custom_menu_name}
                  </h3>
                  <p className='text-sm text-gray-500'>
                    {'menu_price' in menu
                      ? menu.menu_price
                      : menu.custom_menu_price}
                  </p>
                </div>
              </div>
              <div className='flex items-center gap-2'>
                <Button
                  variant='outline'
                  size='icon'
                  className='h-8 w-8'
                  onClick={() =>
                    decreaseQuantity(
                      'menu_id' in menu ? menu.menu_id : menu.custom_menu_id
                    )
                  }
                  type='button'
                >
                  <Minus className='h-4 w-4' />
                </Button>
                <span className='w-8 text-center font-medium'>
                  {menuQuantities[
                    'menu_id' in menu ? menu.menu_id : menu.custom_menu_id
                  ] || 0}
                </span>
                <Button
                  variant='outline'
                  size='icon'
                  className='h-8 w-8'
                  onClick={() =>
                    increaseQuantity(
                      'menu_id' in menu ? menu.menu_id : menu.custom_menu_id
                    )
                  }
                  type='button'
                >
                  <Plus className='h-4 w-4' />
                </Button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 선물 이름 */}
      <div className='space-y-2'>
        <Label className='text-sm font-medium text-gray-700'>선물 이름</Label>
        <Input
          type='text'
          placeholder='선물 이름을 입력해주세요.'
          value={giftTitle}
          onChange={(e) => setGiftTitle(e.target.value)}
          required
        />
      </div>

      {/* 선물 메시지 */}
      <div className='space-y-2 mb-30'>
        <Label className='text-sm font-medium text-gray-700'>선물 메세지</Label>
        <Input
          type='text'
          placeholder='선물 메세지를 입력해주세요.'
          value={giftMessage}
          onChange={(e) => setGiftMessage(e.target.value)}
        />
      </div>

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
            <Button
              type='submit'
              className='w-full flex items-center justify-center gap-2 py-6 text-lg bg-primary hover:bg-primary/90'
            >
              <Gift className='h-5 w-5' />
              선물하기
            </Button>
          </div>
        </div>
      )}

      {/* 모달 컴포넌트들 */}
      {isFavoriteMarketShow && (
        <FavoriteMarketList
          setMenuList={setMenuList}
          setSelectedMarketId={setSelectedMarketId}
          setCustomMenuList={setCustomMenuList}
          setIsFavoriteMarketShow={setIsFavoriteMarketShow}
          setMarket={setMarket}
        />
      )}
    </form>
  );
};

'use client';

// import { useHandleFavoriteMarkets } from '../api/HandleFavoriteMarkets';
import { Button } from '@/components/ui/button';
import { useState } from 'react';
import { Store, Search, X } from 'lucide-react';
import Image from 'next/image';
import { useFetchFavoriteStores } from '@/entity/store/api/useFetchFavoriteStores';
import { TMenu, TCustomMenu, TMarketResponse } from '@/entity/store/model/menu';

interface FavoriteMarketListProps {
  setMenuList: React.Dispatch<React.SetStateAction<TMenu[]>>;
  setCustomMenuList: React.Dispatch<React.SetStateAction<TCustomMenu[]>>;
  setIsFavoriteMarketShow: React.Dispatch<React.SetStateAction<boolean>>;
  setMarket: React.Dispatch<React.SetStateAction<TMarketResponse | null>>;
  setSelectedMarketId: React.Dispatch<React.SetStateAction<number | null>>;
}

const FavoriteMarketList = ({
  setMenuList,
  setCustomMenuList,
  setIsFavoriteMarketShow,
  setMarket,
  setSelectedMarketId,
}: FavoriteMarketListProps) => {
  const { favoriteStores } = useFetchFavoriteStores();
  console.log(favoriteStores);
  const [query, setQuery] = useState<string>('');

  const filteredMarkets = favoriteStores?.filter((market) => market.place_name.includes(query));

  const handleMarketSelect = (market: TMarketResponse) => {
    setSelectedMarketId(market.id);
    setCustomMenuList([]);
    setMarket(market);
    setIsFavoriteMarketShow((toggle) => !toggle);
  };

  return (
    <div className='fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-end z-50'>
      <div className='bg-white rounded-t-2xl w-full max-w-md p-6 h-[70vh] flex flex-col'>
        {/* 헤더 */}
        <div className='flex items-center justify-between mb-6'>
          <div className='flex items-center gap-2'>
            <Store className='h-5 w-5 text-primary' />
            <h2 className='text-xl font-bold text-gray-900'>매장 선택</h2>
          </div>
          <button
            className='p-2 hover:bg-gray-100 rounded-full transition-colors'
            onClick={() => setIsFavoriteMarketShow(false)}
          >
            <X className='h-5 w-5 text-gray-500' />
          </button>
        </div>

        {/* 검색바 */}
        <div className='relative mb-6'>
          <Search className='absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400' />
          <input
            type='text'
            placeholder='매장명으로 검색'
            className='w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/20'
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>

        {/* 매장 목록 */}
        <div className='flex-1 overflow-y-auto space-y-4'>
          {filteredMarkets?.map((market) => (
            <Button
              key={market.id}
              variant='ghost'
              className='w-full justify-start p-4 hover:bg-gray-50'
              onClick={() => handleMarketSelect(market)}
            >
              <div className='flex items-center gap-3'>
                <div className='relative w-16 h-16 rounded-lg overflow-hidden'>
                  // Fixme, 이미지 주소 수정필요
                  <Image src='' alt={market.place_name} fill className='object-cover' />
                </div>
                <div className='text-left'>
                  <p className='font-medium text-gray-900'>{market.place_name}</p>
                  <p className='text-sm text-gray-500'>{market.address_name}</p>
                </div>
              </div>
            </Button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default FavoriteMarketList;

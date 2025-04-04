'use client';

import { useState } from 'react';
import { Map, useKakaoLoader } from 'react-kakao-maps-sdk';
import { Button } from '@/components/ui/button';
import useDebounce from '@/shared/utils/useDebounce';
import Places from './Places';
import { Coordinates, Marker } from '../model/marker';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible';
import { SquareMenu } from 'lucide-react';
import { useMapStore } from '@/store/useMapStore';
import MyStores from './MyStores';
import { useMarkersStore } from '@/store/useMarkerStore';
import { CATEGORY_MAP, TCategory } from '../model/category';
import MyGifts from './MyGifts';
import Image from 'next/image';
import { useSearchParams } from 'next/navigation';

export const KakaoMap = () => {
  useKakaoLoader({
    appkey: process.env.NEXT_PUBLIC_KAKAO_MAP_KEY!,
    libraries: ['services', 'clusterer', 'drawing'],
  });

  const params = useSearchParams();
  const lat = params.get('lat');
  const lng = params.get('lng');
  const initCategory = params.get('category');
  const { map, setMap } = useMapStore();
  const { markers, setMarkers } = useMarkersStore();
  const [category, setCategory] = useState<TCategory>(
    initCategory ? (initCategory as TCategory) : null,
  );
  const [center, setCenter] = useState<Coordinates>({
    lat: lat ? Number(lat) : 35.095326,
    lng: lng ? Number(lng) : 128.855668,
  });
  const [placesVisible, setPlacesVisible] = useState(true);

  const handleCategory = (value: TCategory) => {
    //FIXME - 추후 토글이나 다른 방식으로 변경경
    if (category !== value) {
      setCategory(value);
      if (value === CATEGORY_MAP.STORES) {
        searchStores();
      } else {
        setMarkers([]);
      }
    } else {
      setCategory(null);
      setMarkers([]);
    }
  };

  const changeCenter = (position: Coordinates) => {
    setCenter(position);
  };

  const handleCenterChanged = useDebounce(async () => {
    if (!map) return;
    if (category === CATEGORY_MAP.STORES) {
      searchStores();
    }
  }, 500);

  const searchStores = () => {
    const ps = new kakao.maps.services.Places();

    ps.categorySearch(
      'FD6',
      (data, status) => {
        if (status === kakao.maps.services.Status.ZERO_RESULT) {
          //TODO - 값이 없을 때 어떻게 처리해야할지?
          setMarkers([]);
          return;
        }
        if (status === kakao.maps.services.Status.OK) {
          const bounds = new kakao.maps.LatLngBounds();
          const markers: Marker[] = [];
          for (let i = 0; i < data.length; i++) {
            const { x, y, address_name, id, place_url, place_name } = data[i];

            markers.push({
              id,
              position: {
                lat: Number(y),
                lng: Number(x),
              },
              place_name,
              address_name,
              place_url,
            });
            bounds.extend(new kakao.maps.LatLng(Number(y), Number(x)));
          }
          setMarkers(markers);
        }
      },
      {
        bounds: map?.getBounds(),
        useMapBounds: true,
        location: map?.getCenter(),
        sort: kakao.maps.services.SortBy.DISTANCE,
      },
    );
  };

  const [isOpen, setIsOpen] = useState(false);

  const handleMapClick = () => {
    // 맵 클릭 시 Places 컴포넌트 숨기기
    setPlacesVisible(false);
  };

  return (
    <div className="relative w-full h-full">
      <Map
        center={center}
        className="w-full h-full"
        level={3}
        onCreate={setMap}
        onCenterChanged={handleCenterChanged}
        onDrag={handleMapClick}
      >
        <Collapsible
          open={isOpen}
          onOpenChange={setIsOpen}
          className="absolute top-16 right-2 flex flex-col items-end z-10"
        >
          <CollapsibleTrigger asChild>
            <Button variant="ghost">
              <SquareMenu className="size-8" />
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent className="flex flex-col gap-2 mt-2">
            <Button
              variant={'ghost'}
              className="bg-white h-full"
              onClick={() => handleCategory(CATEGORY_MAP.MY_STORE)}
            >
              <div className="flex flex-col items-center gap-1">
                <Image src="/myStore.png" alt="나만의 또갈집" width={24} height={24} />
                <span className="text-xs">또갈집</span>
              </div>
            </Button>
            <Button
              variant={'ghost'}
              className="bg-white h-full"
              onClick={() => handleCategory(CATEGORY_MAP.STORES)}
            >
              <div className="flex flex-col items-center gap-1">
                <Image src="/restaurant.png" alt="나만의 또갈집" width={24} height={24} />
                <span className="text-xs">근처가게</span>
              </div>
            </Button>
            <Button
              variant={'ghost'}
              className="bg-white h-full"
              onClick={() => handleCategory(CATEGORY_MAP.GIFT)}
            >
              <div className="flex flex-col items-center gap-1">
                <Image src="/gift.png" alt="나만의 또갈집" width={24} height={24} />
                <span className="text-xs">기프티콘</span>
              </div>
            </Button>
          </CollapsibleContent>
        </Collapsible>
        {markers.length > 0 && (
          <Places
            markers={markers}
            changeCenter={changeCenter}
            isVisible={placesVisible}
            setIsVisible={setPlacesVisible}
          />
        )}
        {category === CATEGORY_MAP.MY_STORE && <MyStores changeCenter={changeCenter} />}
        {category === CATEGORY_MAP.GIFT && <MyGifts changeCenter={changeCenter} />}
      </Map>
    </div>
  );
};

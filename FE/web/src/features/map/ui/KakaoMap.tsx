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

export const KakaoMap = () => {
  useKakaoLoader({
    appkey: process.env.NEXT_PUBLIC_KAKAO_MAP_KEY!,
    libraries: ['services', 'clusterer', 'drawing'],
  });

  const { map, setMap } = useMapStore();
  const [markers, setMarkers] = useState<Marker[]>([]);
  const [category, setCategory] = useState<string | null>(null);
  const [center, setCenter] = useState<Coordinates>({ lat: 35.095326, lng: 128.855668 });

  const handleCategory = () => {
    //FIXME - 추후 토글이나 다른 방식으로 변경경
    if (category !== 'FD6') {
      setCategory('FD6');
      searchStores();
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
    if (category === 'FD6') {
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

  return (
    <div className="relative w-full h-full">
      <Map
        center={center}
        className="w-full h-full"
        level={3}
        onCreate={setMap}
        onCenterChanged={handleCenterChanged}
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
          <CollapsibleContent className="flex flex-col space-y-2">
            <Button className="rounded-md border px-4 py-2 font-mono text-sm shadow-sm">
              나만의 또갈집
            </Button>
            <Button
              className="rounded-md border px-4 py-2 font-mono text-sm shadow-sm"
              onClick={handleCategory}
            >
              근처 가게
            </Button>
            <Button className="rounded-md border px-4 py-2 font-mono text-sm shadow-sm">
              기프티콘 가게
            </Button>
          </CollapsibleContent>
        </Collapsible>
        {markers.length > 0 && <Places markers={markers} changeCenter={changeCenter} />}
      </Map>
    </div>
  );
};

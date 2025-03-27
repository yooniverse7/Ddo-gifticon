'use client';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Marker } from '@/features/map/model/marker';
import { useMapStore } from '@/store/useMapStore';
import { useMarkersStore } from '@/store/useMarkerStore';
import Form from 'next/form';
import { useRef } from 'react';

export const SearchBar = () => {
  const { map } = useMapStore();
  const { setMarkers } = useMarkersStore();
  const keywordRef = useRef<HTMLInputElement>(null);

  const handleSearchBtn = () => {
    const value = keywordRef.current?.value;

    if (!value) return;

    searchByKeyword(value.trim());
  };

  const searchByKeyword = (keyword: string) => {
    const ps = new kakao.maps.services.Places();

    ps.keywordSearch(
      keyword,
      (data, status, _pagination) => {
        if (status === kakao.maps.services.Status.OK) {
          // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
          // LatLngBounds 객체에 좌표를 추가합니다
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
          console.log(markers);
          // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
          map?.setBounds(bounds);
        }
      },
      {
        location: map?.getCenter(),
        sort: kakao.maps.services.SortBy.DISTANCE,
      },
    );
  };

  return (
    <div className="fixed top-0 w-full h-12 z-30 bg-white">
      <Form action={''} className="flex items-center p-1">
        <Input ref={keywordRef} type="text" placeholder="검색어를 입력하세요" />
        <Button onClick={handleSearchBtn}>검색</Button>
      </Form>
    </div>
  );
};

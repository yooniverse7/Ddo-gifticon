'use client';

import Link from 'next/link';
import React, { useCallback, useEffect } from 'react';
import { Coordinates, Marker } from '../model/marker';
import { URL } from '@/shared/constants/url';
import { Button } from '@/components/ui/button';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import Markers from './Markers';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';
import { encodeUrl } from '@/shared/utils/encodeUrl';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
} from '@/components/ui/carousel';
import { Card, CardContent } from '@/components/ui/card';
import { useCarouselWithMarker } from '@/shared/hooks/useCarouselWithMarker';
import { useMapStore } from '@/store/useMapStore';
import { useMarkersStore } from '@/store/useMarkerStore';

interface PlacesProps {
  markers: Marker[];
  changeCenter: (position: Coordinates) => void;
  isVisible?: boolean;
  setIsVisible?: (visible: boolean) => void;
}

export default function Places({
  markers,
  changeCenter,
  isVisible: propIsVisible,
  setIsVisible: propSetIsVisible,
}: PlacesProps) {
  const {
    selectedItem: info,
    api,
    setApi,
    handleMarkerClick: handleMarkerFromHook,
    isVisible: hookIsVisible,
    setIsVisible: hookSetIsVisible,
    setSelectedItem,
  } = useCarouselWithMarker<Marker>({
    items: markers,
    changeCenter,
    initialVisible: propIsVisible !== undefined ? propIsVisible : true,
  });

  // isVisible과 setIsVisible이 props로 전달된 경우 그것을 사용, 아니면 훅의 상태 사용
  const isVisible = propIsVisible !== undefined ? propIsVisible : hookIsVisible;
  const setIsVisible = propSetIsVisible || hookSetIsVisible;
  const { map } = useMapStore();
  const { setMarkers } = useMarkersStore();

  // 커스텀 마커 클릭 핸들러
  const handleMarker = useCallback(
    (marker: Marker) => {
      // 먼저 선택된 아이템과 중심 좌표를 설정
      setSelectedItem(marker);
      changeCenter(marker.position);

      // 리스트를 표시하고 Carousel 스크롤
      if (api) {
        const index = markers.findIndex((item) => item.id === marker.id);
        if (index !== -1) {
          api.scrollTo(index);
        }
      }
      if (setIsVisible) {
        setIsVisible(true);
      }
    },
    [api, markers, changeCenter, setIsVisible, setSelectedItem]
  );

  // 지도 드래그 시 리스트 숨기기
  useEffect(() => {
    if (!map) return;

    const dragStartListener = () => {
      if (setIsVisible) {
        setIsVisible(false);
      }
    };

    kakao.maps.event.addListener(map, 'dragstart', dragStartListener);

    return () => {
      kakao.maps.event.removeListener(map, 'dragstart', dragStartListener);
    };
  }, [map, setIsVisible]);

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
      }
    );
  };

  return (
    <>
      <Markers
        markers={markers}
        onClick={handleMarker}
        selectedMarker={info}
        imgSrc={'/restaurant.png'}
      />
      {isVisible ? (
        <FadeUpContainer
          className={`absolute bottom-0 flex flex-col w-full h-60 p-2 z-10 rounded-lg bg-white overflow-y-auto will-change-transform`}
        >
          <div className='flex justify-center'>
            <button
              className='cursor-pointer'
              onClick={() => setIsVisible(false)}
            >
              <ChevronsDown />
            </button>
          </div>
          <Carousel
            draggable
            className='w-full h-full flex items-center pl-2'
            setApi={setApi}
          >
            <CarouselContent>
              {markers.length > 0 ? (
                markers.map((marker) => (
                  <CarouselItem
                    key={`${marker.id}${marker.id}`}
                    className='basis-[90%] p-4'
                  >
                    <Card onClick={() => handleMarker(marker)}>
                      <CardContent className='p-4 pb-3 flex items-start'>
                        <div className='flex-1 pr-4'>
                          <h2 className='text-xl font-normal mb-1'>
                            {marker.place_name}
                          </h2>
                          <div className='flex items-center gap-1 mb-1.5'>
                            <span className='mx-1 text-gray-300'>•</span>
                            <span className='text-gray-600 text-sm'>
                              {marker.address_name}
                            </span>
                          </div>
                          <Link
                            target='_blank'
                            className='text-blue-500 text-sm hover:underline'
                            href={marker.place_url ?? ''}
                          >
                            상세 페이지
                          </Link>
                        </div>
                        <Link
                          href={`${URL.store_register}?data=${encodeUrl(
                            JSON.stringify([
                              marker.place_name,
                              marker.address_name,
                            ])
                          )}`}
                          className='ml-auto'
                        >
                          <Button className='whitespace-nowrap bg-[#FBBC05] hover:bg-[#FBBC05]/80'>
                            맛집 등록
                          </Button>
                        </Link>
                      </CardContent>
                    </Card>
                  </CarouselItem>
                ))
              ) : (
                <CarouselItem className='basis-full'>
                  <div className='bg-white rounded-lg p-6 text-center shadow-[0_2px_8px_rgba(0,0,0,0.15)]'>
                    <p className='text-gray-500'>표시할 맛집이 없습니다.</p>
                  </div>
                </CarouselItem>
              )}
            </CarouselContent>
          </Carousel>
        </FadeUpContainer>
      ) : (
        <>
          <div
            className='absolute bottom-10 left-1/2 -translate-x-1/2 z-10 py-2 px-3 border bg-[#FBBC05] text-white rounded-4xl cursor-pointer'
            onClick={searchStores}
          >
            이 지역 검색하기
          </div>
          <div className='absolute bottom-0 left-1/2 -translate-x-1/2 z-10 '>
            <button
              className='cursor-pointer'
              onClick={() => setIsVisible(true)}
            >
              <ChevronsUp />
            </button>
          </div>
        </>
      )}
    </>
  );
}

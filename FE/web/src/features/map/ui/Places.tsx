'use client';

import Link from 'next/link';
import React, { useState, useEffect } from 'react';
import { Coordinates, Marker } from '../model/marker';
import { URL } from '@/shared/constants/url';
import { Button } from '@/components/ui/button';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import Markers from './Markers';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';
import { encodeUrl } from '@/shared/utils/encodeUrl';
import { Carousel, CarouselContent, CarouselItem, CarouselApi } from '@/components/ui/carousel';
import { Card, CardContent } from '@/components/ui/card';

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
  const [info, setInfo] = useState<Marker | null>(null);
  const [localIsVisible, setLocalIsVisible] = useState(true);

  const isVisible = propIsVisible !== undefined ? propIsVisible : localIsVisible;
  const setIsVisible = propSetIsVisible || setLocalIsVisible;

  const [api, setApi] = useState<CarouselApi>();
  const [current, setCurrent] = useState(0);

  useEffect(() => {
    if (!api) return;

    const onSelect = () => {
      setCurrent(api.selectedScrollSnap());
    };

    api.on('select', onSelect);

    return () => {
      api.off('select', onSelect);
    };
  }, [api]);

  useEffect(() => {
    if (markers.length > 0 && current >= 0 && current < markers.length && isVisible) {
      setInfo(markers[current]);
      changeCenter(markers[current].position);
    }
  }, [current, markers, changeCenter, isVisible]);

  const handleMarker = (marker: Marker) => {
    setInfo(marker);
    setIsVisible(true);
    changeCenter(marker.position);

    if (api) {
      const index = markers.findIndex((m) => m.id === marker.id);
      if (index !== -1) {
        api.scrollTo(index);
      }
    }
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
          <div className="flex justify-center">
            <button className="cursor-pointer" onClick={() => setIsVisible(false)}>
              <ChevronsDown />
            </button>
          </div>
          <Carousel className="w-full h-full flex items-center pl-2" setApi={setApi}>
            <CarouselContent>
              {markers.length > 0 ? (
                markers.map((marker) => (
                  <CarouselItem key={marker.id} className="basis-[90%] p-4">
                    <Card>
                      <CardContent className="p-4 pb-3 flex items-start">
                        <div className="flex-1 pr-4">
                          <h2 className="text-xl font-semibold mb-1">{marker.place_name}</h2>
                          <div className="flex items-center gap-1 mb-1.5">
                            <span className="mx-1 text-gray-300">•</span>
                            <span className="text-gray-600 text-sm">{marker.address_name}</span>
                          </div>
                          <Link
                            target="_blank"
                            className="text-blue-500 text-sm hover:underline"
                            href={marker.place_url ?? ''}
                          >
                            상세 페이지
                          </Link>
                        </div>
                        <Link href={URL.store_register} className="ml-auto">
                          <Button
                            onClick={(e) => {
                              e.stopPropagation();
                              console.log(
                                encodeUrl(
                                  JSON.stringify({
                                    place_name: marker.place_name,
                                    address_name: marker.address_name,
                                  }),
                                ),
                              );
                            }}
                            className="whitespace-nowrap"
                          >
                            맛집 등록
                          </Button>
                        </Link>
                      </CardContent>
                    </Card>
                  </CarouselItem>
                ))
              ) : (
                <CarouselItem className="basis-full">
                  <div className="bg-white rounded-lg p-6 text-center shadow-[0_2px_8px_rgba(0,0,0,0.15)]">
                    <p className="text-gray-500">표시할 맛집이 없습니다.</p>
                  </div>
                </CarouselItem>
              )}
            </CarouselContent>
          </Carousel>
        </FadeUpContainer>
      ) : (
        <div className="absolute bottom-0 left-1/2 -translate-x-1/2 z-10 ">
          <button className="cursor-pointer" onClick={() => setIsVisible(true)}>
            <ChevronsUp />
          </button>
        </div>
      )}
    </>
  );
}

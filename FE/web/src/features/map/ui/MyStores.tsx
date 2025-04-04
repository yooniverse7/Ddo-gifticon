'use client';

import React, { useState } from 'react';
import { Coordinates, Marker } from '../model/marker';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import { useFetchFavoriteStores } from '@/entity/store/api/useFetchFavoriteStores';
import Image from 'next/image';
import Markers from './Markers';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';
import { Carousel, CarouselContent, CarouselItem } from '@/components/ui/carousel';
import { Card, CardContent } from '@/components/ui/card';

interface PlacesProps {
  changeCenter: (position: Coordinates) => void;
}

export default function MyStores({ changeCenter }: PlacesProps) {
  const [info, setInfo] = useState<Marker | null>(null);
  const { favoriteStores } = useFetchFavoriteStores();

  const [isVisible, setIsVisible] = useState(true);
  const handleMarker = (marker: Marker) => {
    setInfo(marker);
    changeCenter(marker.position);
    setIsVisible(true);
  };

  return (
    <>
      <Markers
        onClick={handleMarker}
        imgSrc="/myStore.png"
        markers={favoriteStores.map(
          ({ address_name, id, place_name, position, visited_count }) => ({
            address_name,
            id,
            place_name,
            place_url: address_name,
            position,
            visited_count,
          }),
        )}
        selectedMarker={info}
      />
      {isVisible ? (
        <FadeUpContainer
          className={`absolute bottom-0 flex flex-col w-full h-60 p-2 z-10 rounded-lg bg-white overflow-y-auto will-change-transform`}
        >
          <div className="flex justify-center">
            <button onClick={() => setIsVisible(false)}>
              <ChevronsDown />
            </button>
          </div>
          <Carousel className="w-full h-full flex items-center pl-2">
            <CarouselContent>
              {favoriteStores.length > 0 ? (
                favoriteStores.map((store) => (
                  <CarouselItem key={store.id} className="basis-[90%] p-4 ">
                    <Card
                      onClick={() => {
                        changeCenter(store.position);
                        setInfo({
                          address_name: store.address_name,
                          id: store.id,
                          place_name: store.place_name,
                          position: store.position,
                          visited_count: store.visited_count,
                        });
                      }}
                    >
                      <CardContent className="p-4 pb-3 flex items-start">
                        <div className="flex-1 pr-4">
                          <h2 className="text-xl font-semibold mb-1">{store.place_name}</h2>
                          <div className="flex items-center gap-1 mb-1.5">
                            <span className="mx-1 text-gray-300">•</span>
                            <span className="text-gray-600 text-sm">{store.address_name}</span>
                          </div>
                          <div className="flex items-center gap-1 mb-1.5">
                            <span className="mx-1 text-gray-300">•</span>
                            <span className="text-gray-600 text-sm">방문 횟수 : </span>
                            <span className="text-lg">{store.visited_count}</span>
                          </div>
                        </div>

                        <div className="w-20 h-20 relative flex-shrink-0">
                          <Image
                            src={store.main_image_url || '/restaurant.png'}
                            alt={store.place_name}
                            fill
                            className="object-cover rounded-md"
                          />
                        </div>
                      </CardContent>
                    </Card>
                  </CarouselItem>
                ))
              ) : (
                <CarouselItem className="basis-full">
                  <div className="bg-white rounded-lg p-6 text-center shadow-[0_2px_8px_rgba(0,0,0,0.15)]">
                    <p className="text-gray-500">저장된 맛집이 없습니다.</p>
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

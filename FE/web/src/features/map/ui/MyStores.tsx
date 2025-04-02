'use client';

import React, { useState } from 'react';
import { Coordinates, Marker } from '../model/marker';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import { useFetchFavoriteStores } from '@/entity/store/api/useFetchFavoriteStores';
import Image from 'next/image';
import Markers from './Markers';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';

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
          className={`absolute bottom-0 flex flex-col w-full h-80 p-2 z-10 rounded-lg bg-white overflow-y-auto will-change-transform`}
        >
          <div className="flex justify-center">
            <button onClick={() => setIsVisible(false)}>
              <ChevronsDown />
            </button>
          </div>
          {favoriteStores.map((marker) => (
            <div
              className="flex justify-between items-center px-2 border-[0.6px] border-black"
              key={marker.id}
              onClick={() => {
                changeCenter(marker.position);
                setInfo({
                  address_name: marker.address_name,
                  id: marker.id,
                  place_name: marker.place_name,
                  position: marker.position,
                  visited_count: marker.visited_count,
                });
              }}
            >
              <div className="flex flex-col">
                <h2 className="font-semibold text-lg w-48 truncate">{marker.place_name}</h2>
                <p className="text-sm ">{marker.address_name}</p>
                <p>방문 횟수: {marker.visited_count}</p>
              </div>
              <div>
                <Image src={marker.main_image_url} width={120} height={120} alt="이미지" />
              </div>
            </div>
          ))}
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

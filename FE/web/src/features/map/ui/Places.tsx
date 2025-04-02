'use client';

import Link from 'next/link';
import React, { useState } from 'react';
import { Coordinates, Marker } from '../model/marker';
import { URL } from '@/shared/constants/url';
import { Button } from '@/components/ui/button';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import Markers from './Markers';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';

interface PlacesProps {
  markers: Marker[];
  changeCenter: (position: Coordinates) => void;
}

export default function Places({ markers, changeCenter }: PlacesProps) {
  const [info, setInfo] = useState<Marker | null>(null);
  const [isVisible, setIsVisible] = useState(true);

  const handleMarker = (marker: Marker) => {
    setInfo(marker);
    setIsVisible(true);
    changeCenter(marker.position);
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
          className={`absolute bottom-0 flex flex-col w-full h-80 p-2 z-10 rounded-lg bg-white overflow-y-auto will-change-transform`}
        >
          <div className="flex justify-center">
            <button className="cursor-pointer" onClick={() => setIsVisible(false)}>
              <ChevronsDown />
            </button>
          </div>
          {markers.map((marker) => (
            <div
              className="flex justify-between items-center px-2 border-[0.6px] border-black"
              onClick={() => {
                setInfo(marker);
                changeCenter(marker.position);
              }}
              key={marker.id}
            >
              <div className="flex flex-col">
                <h2 className="font-semibold text-lg w-48 truncate">{marker.place_name}</h2>
                <p className="text-sm ">{marker.address_name}</p>
                <Link target="_blank" className="text-base" href={marker.place_url ?? ''}>
                  상세 페이지
                </Link>
              </div>
              <Link href={URL.store_register}>
                <Button>맛집 등록</Button>
              </Link>
            </div>
          ))}
        </FadeUpContainer>
      ) : (
        <div className="absolute bottom-0 left-1/2 -translate-x-1/2 z-10 ">
          <button className=" cursor-pointer" onClick={() => setIsVisible(true)}>
            <ChevronsUp />
          </button>
        </div>
      )}
    </>
  );
}

'use client';

import Link from 'next/link';
import React, { useState } from 'react';
import { CustomOverlayMap, MapMarker } from 'react-kakao-maps-sdk';
import { Coordinates, Marker } from '../model/marker';
import { AnimatePresence, motion } from 'motion/react';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import { useFetchFavoriteStores } from '@/features/favoriteStores/api/useFetchFavoriteStores';
import Image from 'next/image';

interface PlacesProps {
  changeCenter: (position: Coordinates) => void;
}

export default function MyStores({ changeCenter }: PlacesProps) {
  const [info, setInfo] = useState<Marker | null>(null);
  const { favoriteStores } = useFetchFavoriteStores();

  const [isVisible, setIsVisible] = useState(true);

  return (
    <>
      {favoriteStores.map(({ address_name, id, place_name, position, visited_count }) => (
        <CustomOverlayMap key={id + 'marker'} position={position}>
          <MapMarker
            onClick={() => {
              setInfo({
                address_name,
                id,
                place_name,
                place_url: address_name,
                position,
                visited_count,
              });
              changeCenter(position);
              setIsVisible(true);
            }}
            position={position}
            image={{
              src: `/restaurant.png`,
              size: {
                width: info?.id === id ? 44 : 22,
                height: info?.id === id ? 52 : 26,
              },
              options: {
                offset: {
                  x: info?.id === id ? 19 : 9,
                  y: info?.id === id ? 52 : 26,
                }, // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                alt: `음식점`,
              },
            }}
          ></MapMarker>
        </CustomOverlayMap>
      ))}
      <AnimatePresence initial={isVisible}>
        {isVisible ? (
          <motion.div
            initial={{ transform: 'translateY(100%)' }}
            animate={{ transform: 'translateY(0px)' }}
            exit={{ transform: 'translateY(100%)' }}
            transition={{ duration: 0.5 }}
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
          </motion.div>
        ) : (
          <div className="absolute bottom-0 left-1/2 -translate-x-1/2 z-10 ">
            <motion.button
              className="w-4 h-4 bg-white cursor-pointer"
              onClick={() => setIsVisible(true)}
            >
              <ChevronsUp />
            </motion.button>
          </div>
        )}
      </AnimatePresence>
    </>
  );
}

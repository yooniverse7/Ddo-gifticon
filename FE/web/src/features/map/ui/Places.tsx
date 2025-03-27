'use client';

import Link from 'next/link';
import React, { useState } from 'react';
import { CustomOverlayMap, MapMarker } from 'react-kakao-maps-sdk';
import { Coordinates, Marker } from '../model/marker';
import { URL } from '@/shared/constants/url';
import { Button } from '@/components/ui/button';
import { AnimatePresence, motion } from 'motion/react';
import { ChevronsDown, ChevronsUp } from 'lucide-react';

interface PlacesProps {
  markers: Marker[];
  changeCenter: (position: Coordinates) => void;
}

export default function Places({ markers, changeCenter }: PlacesProps) {
  const [info, setInfo] = useState<Marker | null>(null);
  const [isVisible, setIsVisible] = useState(true);

  return (
    <>
      {markers.map((marker) => (
        <CustomOverlayMap key={marker.id + 'marker'} position={marker.position}>
          <MapMarker
            onClick={() => {
              setInfo(marker);
              setIsVisible(true);
              changeCenter(marker.position);
            }}
            position={marker.position}
            image={{
              src: `/restaurant.png`,
              size: {
                width: info?.id === marker.id ? 44 : 22,
                height: info?.id === marker.id ? 52 : 26,
              },
              options: {
                offset: {
                  x: info?.id === marker.id ? 19 : 9,
                  y: info?.id === marker.id ? 52 : 26,
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
              <motion.button className="cursor-pointer" onClick={() => setIsVisible(false)}>
                <ChevronsDown />
              </motion.button>
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
                  <Link target="_blank" className="text-base" href={marker.place_url}>
                    상세 페이지
                  </Link>
                </div>
                <Link href={URL.store_register}>
                  <Button>맛집 등록</Button>
                </Link>
              </div>
            ))}
          </motion.div>
        ) : (
          <div className="absolute bottom-0 left-1/2 -translate-x-1/2 z-10 ">
            <motion.button className="w-4 h-4 cursor-pointer" onClick={() => setIsVisible(true)}>
              <ChevronsUp />
            </motion.button>
          </div>
        )}
      </AnimatePresence>
    </>
  );
}

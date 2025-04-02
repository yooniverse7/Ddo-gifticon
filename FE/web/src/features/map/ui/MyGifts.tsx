'use client';

import React, { useState } from 'react';
import { Coordinates } from '../model/marker';
import { ChevronsDown, ChevronsUp } from 'lucide-react';
import { TGift } from '@/entity/gift/model/gift';
import { useFetchGift } from '@/entity/gift/api/useFetchGift';
import { CustomOverlayMap, MapMarker } from 'react-kakao-maps-sdk';
import { FadeUpContainer } from '@/widgets/fadeUpContainer';

interface PlacesProps {
  changeCenter: (position: Coordinates) => void;
}

export default function MyGifts({ changeCenter }: PlacesProps) {
  const [info, setInfo] = useState<TGift | null>(null);
  const { gifts } = useFetchGift();

  const [isVisible, setIsVisible] = useState(true);
  const handleMarker = (marker: TGift) => {
    setInfo(marker);
    changeCenter(marker.position);
    setIsVisible(true);
  };

  return (
    <>
      {gifts.map((gift) => (
        <CustomOverlayMap key={gift.id + 'marker'} position={gift.position}>
          <MapMarker
            onClick={() => handleMarker(gift)}
            position={gift.position}
            image={{
              src: `/gift.png`,
              size: {
                width: info?.id === gift.id ? 66 : 33,
                height: info?.id === gift.id ? 70 : 35,
              },
              options: {
                offset: {
                  x: info?.id === gift.id ? 26 : 13,
                  y: info?.id === gift.id ? 70 : 35,
                }, // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                alt: `음식점`,
              },
            }}
          ></MapMarker>
        </CustomOverlayMap>
      ))}
      {isVisible ? (
        <FadeUpContainer
          className={`absolute bottom-0 flex flex-col w-full h-80 p-2 z-10 rounded-lg bg-white overflow-y-auto will-change-transform`}
        >
          <div className="flex justify-center">
            <button onClick={() => setIsVisible(false)}>
              <ChevronsDown />
            </button>
          </div>
          {gifts.map((gift) => (
            <div
              className="flex justify-between items-center px-2 border-[0.6px] border-black"
              key={gift.id}
              onClick={() => {
                changeCenter(gift.position);
                setInfo(gift);
              }}
            >
              <div className="flex flex-col">
                <h2 className="font-semibold text-lg w-48 truncate">
                  선물준이: {gift.send_user_name}
                </h2>
                <p className="text-sm ">선물제목: {gift.title}</p>
                <p>유효기간: {gift.expiration_date}</p>
              </div>
              {/* <div>
                  <Image src={gift.image} width={120} height={120} alt="이미지" />
                </div> */}
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

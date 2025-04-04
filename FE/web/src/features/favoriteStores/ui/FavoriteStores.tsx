'use client';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useFetchFavoriteStores } from '@/entity/store/api/useFetchFavoriteStores';
import { CATEGORY_MAP } from '@/features/map/model/category';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useState } from 'react';

export const FavoriteStores = () => {
  const { favoriteStores } = useFetchFavoriteStores();
  const router = useRouter();
  const [selectedId, setSelectedId] = useState<number>(-1);
  const [selectedPosition, setSelectedPosition] = useState<{ lat: number; lng: number } | null>(
    null,
  );

  const handleInput = (id: number, position: { lat: number; lng: number }, name: string) => {
    if (selectedId === id) {
      setSelectedId(-1);
      setSelectedPosition(null);
    } else {
      setSelectedId(id);
      setSelectedPosition(position);
    }
  };

  const handleMapNavigation = () => {
    if (!selectedPosition) return;

    // 가게 이름과 카테고리도 함께 전달
    router.push(
      `/?lat=${selectedPosition.lat}&lng=${selectedPosition.lng}&category=${CATEGORY_MAP.MY_STORE}`,
    );
  };

  return (
    <div className="flex flex-col w-full h-full gap-4">
      {favoriteStores?.map(
        ({ id, place_name, position, main_image_url, visited_count, address_name }) => (
          <div className="flex items-center gap-2 px-4" key={id}>
            <Input
              id={address_name}
              type="checkbox"
              onChange={(e) => handleInput(id, position, place_name)}
              checked={selectedId === id}
              className="w-6 h-6 rounded-full"
            />
            <Label htmlFor={address_name} className="w-full">
              <div className="flex justify-between w-full">
                <div className="flex flex-col justify-center gap-1">
                  <p>
                    가게명: <span className="font-semibold text-xl">{place_name}</span>
                  </p>
                  <p>주소: {address_name}</p>
                  <p>
                    방문 횟수: <span className="font-semibold text-xl">{visited_count}</span>
                  </p>
                </div>
                <div>
                  <Image src={main_image_url} width={120} height={120} alt="이미지" />
                </div>
              </div>
            </Label>
          </div>
        ),
      )}
      <div className="flex justify-center gap-4 mt-2">
        <Button onClick={handleMapNavigation} disabled={!selectedPosition} className="px-6">
          지도에서 보기
        </Button>
        <Button variant="destructive" disabled={selectedId === -1}>
          삭제
        </Button>
      </div>
    </div>
  );
};

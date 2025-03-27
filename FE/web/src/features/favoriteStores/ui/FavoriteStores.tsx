'use client';

import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useFetchFavoriteStores } from '../api/useFetchFavoriteStores';

export const FavoriteStores = () => {
  const { favoriteStores } = useFetchFavoriteStores();

  return (
    <div className="flex flex-col w-full h-full gap-4">
      {favoriteStores?.map(
        ({ id, place_name, position, main_image_url, visited_count, address_name }) => (
          <div className="flex items-center gap-2" key={id}>
            <Input id={address_name} type="checkbox" className="w-6 h-6 rounded-full" />
            <Label htmlFor={address_name}>
              <div className="flex flex-col w-full">
                <div key={address_name}>
                  <p>가게명: {place_name}</p>
                  <p>주소: {address_name}</p>
                  <p>방문 횟수: {visited_count}</p>
                  <div>이미지 주소: {main_image_url}</div>
                </div>
              </div>
            </Label>
          </div>
        ),
      )}
    </div>
  );
};

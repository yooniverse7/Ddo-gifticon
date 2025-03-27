import { Button } from '@/components/ui/button';
import { FavoriteStores } from '@/features/favoriteStores';
import React from 'react';

export default function page() {
  return (
    <div className="flex flex-col items-center h-full">
      <h1>또갈집</h1>
      <FavoriteStores />
      <div className="justify-self-end">
        <Button>이동</Button>
        <Button>삭제</Button>
      </div>
    </div>
  );
}

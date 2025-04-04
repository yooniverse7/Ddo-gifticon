import { FavoriteStores } from '@/features/favoriteStores';

export default function page() {
  return (
    <div className="flex flex-col items-center h-full gap-4 pt-8">
      <h1 className="text-4xl font-bold">또갈집 🏠</h1>
      <FavoriteStores />
    </div>
  );
}

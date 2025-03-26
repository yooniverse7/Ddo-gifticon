import { KakaoMap } from '@/features/map';
import { SearchBar } from '@/widgets/searchBar';

export default function Home() {
  return (
    <>
      <SearchBar />
      <KakaoMap />
    </>
  );
}

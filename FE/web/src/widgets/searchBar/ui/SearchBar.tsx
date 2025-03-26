import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

export const SearchBar = () => {
  return (
    <div className="fixed top-0 flex items-center w-full h-12 p-1 gap-2 z-30 bg-white">
      <Input type="text" placeholder="검색어를 입력하세요" />
      <Button>검색</Button>
    </div>
  );
};

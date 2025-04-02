'use client';

import { Search, X } from 'lucide-react';

interface SearchBarProps {
  placeholder: string;
  setQuery: React.Dispatch<React.SetStateAction<string>>;
  setIsShow: React.Dispatch<React.SetStateAction<boolean>>;
}

const SearchBar = ({ placeholder, setQuery, setIsShow }: SearchBarProps) => {
  return (
    <div className='relative w-full'>
      <Search className='absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400' />
      <input
        type='text'
        placeholder={placeholder}
        className='w-full pl-10 pr-10 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/20'
        onChange={(e) => setQuery(e.target.value)}
      />
      <button
        className='absolute right-3 top-1/2 transform -translate-y-1/2 p-1 hover:bg-gray-100 rounded-full transition-colors'
        onClick={() => setIsShow(false)}
      >
        <X className='h-5 w-5 text-gray-500' />
      </button>
    </div>
  );
};

export default SearchBar;

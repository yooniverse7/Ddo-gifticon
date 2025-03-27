import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import React, { useRef } from 'react';

interface Props {
  addCustomMenu: (menu: string, price: number) => void;
}

export const CustomMenuForm = ({ addCustomMenu }: Props) => {
  const nameRef = useRef<HTMLInputElement>(null);
  const priceRef = useRef<HTMLInputElement>(null);

  const handleButton = () => {
    const name = nameRef.current?.value || '';
    const price = Number(priceRef.current?.value) || 0;

    if (!name || !price) {
      return;
    }

    addCustomMenu(name, price);
  };

  return (
    <section className="flex justify-between items-center w-full px-8">
      <div className="flex flex-col w-2/5">
        <Label htmlFor="menuName">메뉴 이름</Label>
        <Input
          ref={nameRef}
          type="text"
          id="menuName"
          name="menuName"
          placeholder="메뉴 이름이 뭔가요?"
        />
      </div>
      <div className="flex flex-col w-2/5">
        <Label htmlFor="menuPrice">가격</Label>
        <Input
          ref={priceRef}
          type="number"
          id="menuPrice"
          name="menuPrice"
          placeholder="가격 형성이 어떻게 되어있쬬?"
        />
      </div>
      <Button onClick={handleButton}>추가</Button>
    </section>
  );
};

import React from 'react';
import Form from 'next/form';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';

export const GiftForm = () => {
  return (
    <section className="flex flex-col justify-between w-full h-full">
      <div className="flex justify-center h-8 mt-8">
        <h1>선물하기</h1>
      </div>
      <Form action={''} className="h-full">
        <Input type="file" />
        <div>
          <Label htmlFor="title">제목 *</Label>
          <Input type="text" id="title" name="title" placeholder="제목을 입력해주세요" required />
        </div>
        <Separator />
        <div>
          <Label htmlFor="receiver">받는 사람 *</Label>
          <select id="receiver" name="receiver" required>
            <option>선택</option>
            <option>010-1111-2222</option>
            <option>010-1111-2224</option>
            <option>010-1111-2223</option>
          </select>
        </div>
        <div>
          <Label htmlFor="menu">메뉴 *</Label>
          <div>맛집 등록 때 저장된 메뉴판</div>
        </div>
        <div>
          <Label htmlFor="message">메세지 *</Label>
          <textarea id="message" name="message" placeholder="메세지로 마음을 전하세요!" required />
        </div>
      </Form>
      <Button type="submit" className="w-full h-20">
        (돈 얼마) 결제하기
      </Button>
    </section>
  );
};

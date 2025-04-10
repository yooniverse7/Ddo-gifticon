import React, { useState, useRef, useCallback } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { axiosInstance } from '@/shared/api/axiosInstance';
import { Label } from '@/components/ui/label';

interface ImageItem {
  src: string;
  alt: string;
}

interface CustomMenuImageSelectorProps {
  onImagesChange: (image: ImageItem) => void;
}

const CustomMenuImageSelector = ({
  onImagesChange,
}: CustomMenuImageSelectorProps) => {
  const [image, setImage] = useState<ImageItem>();
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleGenerationImage = useCallback(
    async (theme: string) => {
      try {
        // 1. 서버에서 이미지 URL 가져오기
        const response = await axiosInstance.get(
          `/api/gift/themeImage?theme=${theme}`
        );
        const imageUrl = response.data.content;

        // 2. 이미지 다운로드 및 File 변환
        const imageResponse = await fetch(imageUrl);
        if (!imageResponse.ok) throw new Error('이미지 다운로드 실패');

        const blob = await imageResponse.blob();
        const fileName = imageUrl.split('/').pop() || 'generated_image.jpg';

        // 3. Blob을 Base64로 변환
        const base64Data = await new Promise<string>((resolve, reject) => {
          const reader = new FileReader();
          reader.onloadend = () => resolve(reader.result as string); // Base64 데이터 반환
          reader.onerror = reject;
          reader.readAsDataURL(blob); // Blob 데이터를 Base64로 읽음
        });

        // 3. 상태 업데이트
        const newImage = {
          src: base64Data, // Blob URL 생성
          alt: theme,
        };
        setImage(newImage);
        // 4. 부모 컴포넌트에 File 객체 전달
        onImagesChange({ src: newImage.src, alt: fileName });
      } catch (error) {
        console.error('이미지 생성 오류:', error);
      }
    },
    [onImagesChange]
  );

  // 파일 읽기 유틸리티 함수
  const readFile = (file: File): Promise<string> =>
    new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (e) =>
        e.target?.result ? resolve(e.target.result.toString()) : reject();
      reader.onerror = () => reject();
      reader.readAsDataURL(file);
    });

  // 이미지 업로드 핸들러
  const handleImageUpload = useCallback(async () => {
    const file = fileInputRef.current?.files?.[0];
    if (!file) return;

    try {
      const src = await readFile(file);
      const newImage = { src, alt: file.name };
      setImage(newImage);
      onImagesChange(newImage); // 배열에 단일 이미지 전달
    } catch (error) {
      console.error('이미지 업로드 실패:', error);
    }
  }, [onImagesChange]);

  // 이미지 삭제 핸들러
  const handleDeleteImage = () => {
    setImage(undefined);
    onImagesChange({ src: '', alt: '' }); // 빈 값 전달
    // input 값을 초기화
    if (fileInputRef.current) {
      fileInputRef.current.value = ''; // 파일 입력 필드 초기화
    }
  };

  return (
    <div className='space-y-4'>
      <Card className='relative'>
        {image && (
          <button
            onClick={handleDeleteImage}
            className='absolute top-2 right-2 z-10 p-1 bg-black/50 rounded-full hover:bg-black/70 transition-colors'
          >
            <X className='h-4 w-4 text-white' />
          </button>
        )}

        <CardContent
          onClick={() => fileInputRef.current?.click()}
          className='flex aspect-square items-center justify-center p-6 cursor-pointer hover:opacity-90 transition-opacity'
        >
          {image ? (
            <img
              src={image.src}
              alt={image.alt}
              className='w-full h-full object-cover rounded-md'
            />
          ) : (
            <span className='text-2xl font-normal text-center'>
              <span className='text-[#FBBC05]'>사진</span>을 넣어주세요!
            </span>
          )}
        </CardContent>
      </Card>
      <input
        type='file'
        accept='image/*'
        ref={fileInputRef}
        onChange={handleImageUpload}
        className='hidden'
      />
      <div className='space-y-2'>
        <Label className='text-sm font-medium text-[#FBBC05]'>
          이미지 생성
        </Label>
        <div className='grid grid-cols-2 gap-2'>
          {[
            { type: 'birthday', text: '생일' },
            { type: 'comfort', text: '위로' },
            { type: 'congratulations', text: '축하' },
            { type: 'encouragement', text: '응원' },
          ].map(({ type, text }: { type: string; text: string }) => (
            <Button
              onClick={() => handleGenerationImage(type)}
              key={type}
              type='button'
              variant='outline'
              className='w-full flex items-center justify-center p-4 h-auto text-gray-900 border-gray-300 hover:bg-gray-100'
            >
              {text}
            </Button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CustomMenuImageSelector;

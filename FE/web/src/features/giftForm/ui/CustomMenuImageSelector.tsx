import React, { useState, useRef, useCallback } from 'react';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselPrevious,
  CarouselNext,
} from '@/components/ui/carousel';
import { Card, CardContent } from '@/components/ui/card';
import { X } from 'lucide-react';

interface ImageItem {
  src: string;
  alt: string;
}

interface CustomMenuImageSelectorProps {
  onImagesChange: (images: ImageItem[]) => void;
}

const CustomMenuImageSelector = ({ onImagesChange }: CustomMenuImageSelectorProps) => {
  const [images, setImages] = useState<ImageItem[]>([]);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileRead = useCallback((file: File): Promise<ImageItem> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (event) => {
        if (event.target && typeof event.target.result === 'string') {
          resolve({ src: event.target.result, alt: file.name });
        } else {
          reject(new Error('이미지 파일을 읽을 수 없습니다.'));
        }
      };
      reader.onerror = () => reject(new Error('파일 읽기 오류가 발생했습니다.'));
      reader.readAsDataURL(file);
    });
  }, []);

  const handleImageUpload = useCallback(async () => {
    if (!fileInputRef.current) return;

    fileInputRef.current.click();
    fileInputRef.current.onchange = async (e) => {
      const target = e.target as HTMLInputElement | null;
      if (!target?.files?.length) return;

      try {
        const newImages = await Promise.all(Array.from(target.files).map(handleFileRead));
        setImages((prevImages) => {
          const updatedImages = [...prevImages, ...newImages];
          console.log('업로드된 이미지 URL:', updatedImages);
          onImagesChange(updatedImages);
          return updatedImages;
        });
      } catch (error) {
        console.error('이미지 업로드 중 오류 발생:', error);
        // TODO: 사용자에게 에러 메시지 표시
      }
    };
  }, [handleFileRead, onImagesChange]);

  const handleDeleteImage = (index: number) => {
    setImages((prevImages) => {
      const updatedImages = prevImages.filter((_, i) => i !== index);
      onImagesChange(updatedImages);
      return updatedImages;
    });
  };

  return (
    <div className='space-y-4'>
      <h3 className='text-lg font-semibold'>나만의 메뉴 이미지를 선택해주세요</h3>
      <Carousel className='w-full max-w-xs'>
        <CarouselContent>
          {images.length === 0 ? (
            <CarouselItem>
              <div className='p-1'>
                <Card
                  onClick={handleImageUpload}
                  className='cursor-pointer hover:opacity-90 transition-opacity'
                >
                  <CardContent className='flex aspect-square items-center justify-center p-6'>
                    <span className='text-3xl font-semibold text-center'>사진을 넣어주세요!</span>
                  </CardContent>
                </Card>
              </div>
            </CarouselItem>
          ) : (
            images.map((image, index) => (
              <CarouselItem key={index}>
                <div className='p-1'>
                  <Card className='relative'>
                    <button
                      onClick={() => handleDeleteImage(index)}
                      className='absolute top-2 right-2 z-10 p-1 bg-black/50 rounded-full hover:bg-black/70 transition-colors'
                    >
                      <X className='h-4 w-4 text-white' />
                    </button>
                    <CardContent
                      onClick={handleImageUpload}
                      className='flex aspect-square items-center justify-center p-6 cursor-pointer hover:opacity-90 transition-opacity'
                    >
                      <img
                        src={image.src}
                        alt={image.alt}
                        className='w-full h-full object-cover rounded-md'
                      />
                    </CardContent>
                  </Card>
                </div>
              </CarouselItem>
            ))
          )}
        </CarouselContent>
        <CarouselPrevious />
        <CarouselNext />
      </Carousel>
      <input type='file' accept='image/*' ref={fileInputRef} className='hidden' multiple />
    </div>
  );
};

export default CustomMenuImageSelector;

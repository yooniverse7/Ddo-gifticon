'use client';

import { Button } from '@/components/ui/button';
import { handleMessageToRn } from '@/features/permissonRequest/api/handleMessageToRn';

export default function PermissionPage() {
  return (
    <div className='flex flex-col min-h-screen p-6 bg-white'>
      <div className='mb-8'>
        <h1 className='text-xl font-bold text-center'>
          또가게 서비스 이용을 위한 앱 접근권한 안내
        </h1>
      </div>

      <div className='space-y-6 flex-grow'>
        <div className='flex items-start'>
          <div className='w-12 h-12 rounded-full bg-amber-200 flex items-center justify-center flex-shrink-0 mr-4'>
            <span className='text-gray-700 text-sm'>연락처</span>
          </div>
          <div>
            <h2 className='text-base font-medium mb-1'>
              송금하기, 직원등록 서비스 이용을 위해 필요
            </h2>
            <p className='text-sm text-gray-600 mb-1'>
              또가게 서비스는 위치 정보를 수집하여 사용자의 현재 위치를
              확인합니다.
            </p>
            <p className='text-sm text-gray-600'>
              위치 정보 접근 권한을 허용하면 또가게 서비스가 사용자의 위치를
              확인할 수 있습니다.
            </p>
          </div>
        </div>

        <div className='flex items-start'>
          <div className='w-12 h-12 rounded-full bg-amber-200 flex items-center justify-center flex-shrink-0 mr-4'>
            <span className='text-gray-700 text-sm'>위치</span>
          </div>
          <div>
            <h2 className='text-base font-medium mb-1'>
              위치기반 서비스 이용을 위해 필요
            </h2>
            <p className='text-sm text-gray-600 mb-1'>
              또가게 서비스는 연락처 정보를 수집하여 사용자의 연락처를
              확인합니다.
            </p>
            <p className='text-sm text-gray-600'>
              연락처 접근 권한을 허용하면 또가게 서비스가 사용자의 연락처를
              확인할 수 있습니다.
            </p>
          </div>
        </div>

        <div className='flex items-start'>
          <div className='w-12 h-12 rounded-full bg-amber-200 flex items-center justify-center flex-shrink-0 mr-4'>
            <span className='text-gray-700 text-sm'>카메라</span>
          </div>
          <div>
            <h3 className='text-base font-medium mb-1'>
              QR코드 스캔 및 사진 촬영을 위해 필요
            </h3>
            <p className='text-sm text-gray-600 mb-1'>
              또가게 서비스는 카메라를 사용하여 사용자의 카메라를 확인합니다.
            </p>
            <p className='text-sm text-gray-600'>
              카메라 접근 권한을 허용하면 또가게 서비스가 사용자의 카메라를
              확인할 수 있습니다.
            </p>
          </div>
        </div>

        <div className='flex items-start'>
          <div className='w-12 h-12 rounded-full bg-amber-200 flex items-center justify-center flex-shrink-0 mr-4'>
            <span className='text-gray-700 text-sm'>저장소</span>
          </div>
          <div>
            <h3 className='text-base font-medium mb-1'>
              이미지 업로드를 위해 필요
            </h3>
            <p className='text-sm text-gray-600 mb-1'>
              또가게 서비스는 사진 및 동영상을 수집하여 사용자의 사진 및
              동영상을 확인합니다.
            </p>
            <p className='text-sm text-gray-600'>
              사진 및 동영상 접근 권한을 허용하면 또가게 서비스가 사용자의 사진
              및 동영상을 확인할 수 있습니다.
            </p>
          </div>
        </div>
      </div>

      <div className='mt-8 border-t border-gray-200 pt-4'>
        <h3 className='text-sm font-medium mb-1'>접근권한 변경 안내</h3>
        <p className='text-xs text-gray-500 mb-6'>
          휴대전화 설정의 어플리케이션 관리 및 어플리케이션의 관한 설정에서 변경
          가능합니다.
        </p>
      </div>

      <Button
        onClick={handleMessageToRn}
        className='w-full py-4 bg-amber-400 hover:bg-amber-500 text-black font-medium rounded-md'
      >
        확인
      </Button>
    </div>
  );
}

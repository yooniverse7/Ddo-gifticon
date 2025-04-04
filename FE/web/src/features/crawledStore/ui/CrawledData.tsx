import { Separator } from '@/components/ui/separator';
import { CrawledData } from '@/entity/store/api/useFetchCrawledStore';

export const CrawledStore = ({ crawledData }: { crawledData?: CrawledData }) => {
  return (
    <>
      {crawledData && (
        <>
          <div className="flex items-center w-full px-8 gap-4">
            <div className="w-40 h-40 my-4">
              <img
                src={crawledData.main_image_url ?? '/logo.png'}
                height="200"
                width="200"
                className="object-cover"
                alt="메인 이미지"
              />
            </div>
            <div>
              <h2 className="font-semibold text-xl mb-4">{crawledData.place_name}</h2>
              <p>주소 : {crawledData.address_name}</p>
            </div>
          </div>
          <h2 className="font-semibold text-lg">기존 메뉴</h2>
          <div className="flex flex-col w-full max-h-96 px-8 overflow-y-auto">
            <ul>
              {crawledData.menus.length > 0 ? (
                crawledData.menus.map((menu) => (
                  <li key={menu.menu_name}>
                    <div className="flex items-center justify-between">
                      <div className="flex flex-col">
                        <h3 className="font-bold text-xl">{menu.menu_name}</h3>
                        <p>{menu.menu_price}</p>
                      </div>
                    </div>
                    <Separator className="my-2" />
                  </li>
                ))
              ) : (
                <li>메뉴 없음</li>
              )}
            </ul>
          </div>
        </>
      )}
    </>
  );
};

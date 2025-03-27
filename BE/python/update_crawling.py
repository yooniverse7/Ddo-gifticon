import time
import json
from typing import List, Optional
from urllib.parse import urlparse, parse_qs

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager


class RestaurantCrawlingMenuDto:
    def __init__(self):
        self.menuName: str = None
        self.menuDesc: str = None
        self.menuPrice: str = None
        self.menuImage: str = None


class RestaurantCrawlingStoreDto:
    def __init__(self):
        self.placeName: str = None
        self.mainImageUrl: str = None
        self.addressName: str = None
        self.storeInfo: str = None
        self.position_lat: Optional[float] = None
        self.position_lng: Optional[float] = None
        self.menus: List[RestaurantCrawlingMenuDto] = []


def create_webdriver(headless: bool = True) -> webdriver.Chrome:
    chrome_options = Options()
    if headless:
        # Chrome 109+ 에서는 --headless=new 사용
        chrome_options.add_argument("--headless=new")

    # 이미지 로딩 차단
    chrome_prefs = {
        "profile.default_content_setting_values": {
            "images": 2  # 2: 차단 / 1: 허용
        }
    }
    chrome_options.experimental_options["prefs"] = chrome_prefs

    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument('--disable-images')
    chrome_options.add_experimental_option("prefs", {'profile.managed_default_content_settings.images': 2})
    chrome_options.add_argument('--blink-settings=imagesEnabled=false')
    chrome_options.add_argument("--window-size=1920,1080")
    chrome_options.page_load_strategy = "none"
    chrome_options.add_argument(
        "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"
    )

    driver = webdriver.Chrome(
        service=Service(ChromeDriverManager().install()),
        options=chrome_options
    )
    return driver


def wait_and_switch_to_frame(driver: webdriver.Chrome, frame_css: str, timeout: int = 10):
    WebDriverWait(driver, timeout).until(EC.frame_to_be_available_and_switch_to_it((By.CSS_SELECTOR, frame_css)))


def click_element_with_wait(driver: webdriver.Chrome, element, timeout: int = 5):
    WebDriverWait(driver, timeout).until(EC.element_to_be_clickable(element))
    driver.execute_script("arguments[0].click();", element)


def extract_store_info(driver: webdriver.Chrome, wait: WebDriverWait, dto: RestaurantCrawlingStoreDto):
    # 가게명
    try:
        name_el = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "span.GHAhO")))
        dto.placeName = name_el.text.strip()
    except NoSuchElementException:
        dto.placeName = ""

    # 대표 이미지
    try:
        main_img = driver.find_element(By.CSS_SELECTOR, ".fNygA>a>img").get_attribute("src")
        dto.mainImageUrl = main_img
    except NoSuchElementException:
        dto.mainImageUrl = ""

    # 주소
    try:
        address_el = driver.find_element(By.CSS_SELECTOR, "span.LDgIH")
        dto.addressName = address_el.text.strip()
    except NoSuchElementException:
        dto.addressName = ""

    # URL에서 lat, lng 추출하기 (option)
    # 네이버 지도 URL 구조 예시:
    # https://map.naver.com/v5/entry/place/35418577?c=128.854346533083,35.0933107172125,17,0,0,0,dh
    try:
        current_url = driver.current_url
        parsed_url = urlparse(current_url)
        qs = parse_qs(parsed_url.query)
        c_param = qs.get('c', [None])[0]  # "128.854346533083,35.0933107172125,17,0,0,0,dh" 등
        if c_param:
            coords = c_param.split(',')
            if len(coords) >= 2:
                dto.position_lng = float(coords[0])
                dto.position_lat = float(coords[1])
    except Exception:
        # 좌표가 없거나 파싱 실패시 None
        dto.position_lat = None
        dto.position_lng = None

    # 상단 탭 중 "정보" 탭
    # info_tabs = driver.find_elements(By.CSS_SELECTOR, ".flicking-camera > a")
    # info_tab = next((it for it in info_tabs if it.text.strip() == "정보"), None)
    # if info_tab:
    #     click_element_with_wait(driver, info_tab)
    #     WebDriverWait(driver, 3).until(lambda _: info_tab.get_attribute("aria-selected") == "true")
    #     try:
    #         info_el = driver.find_element(By.CSS_SELECTOR, "div.T8RFa")
    #         dto.storeInfo = info_el.text.strip()
    #     except NoSuchElementException:
    #         dto.storeInfo = ""
    # else:
    #     dto.storeInfo = ""


def extract_menu_info(driver: webdriver.Chrome, wait: WebDriverWait, dto: RestaurantCrawlingStoreDto):
    menu_tabs = driver.find_elements(By.CSS_SELECTOR, ".flicking-camera > a")
    menu_tab = next((it for it in menu_tabs if it.text.strip() == "메뉴"), None)
    if not menu_tab:
        print("메뉴 탭이 존재하지 않습니다.")
        return

    # 메뉴 탭 클릭
    click_element_with_wait(driver, menu_tab)
    WebDriverWait(driver, 3).until(lambda _: menu_tab.get_attribute("aria-selected") == "true")

    try:
        menu_elements = driver.find_elements(By.CSS_SELECTOR, "li.E2jtL")
        menu_list: List[RestaurantCrawlingMenuDto] = []

        for menu_el in menu_elements:
            menu_dto = RestaurantCrawlingMenuDto()

            # 메뉴 이름
            try:
                name_el2 = menu_el.find_element(By.CSS_SELECTOR, "span.lPzHi")
                menu_dto.menuName = name_el2.text.strip()
            except NoSuchElementException:
                menu_dto.menuName = ""

            # 메뉴 소개
            try:
                desc_el = menu_el.find_element(By.CSS_SELECTOR, "div.kPogF")
                menu_dto.menuDesc = desc_el.text.strip()
            except NoSuchElementException:
                menu_dto.menuDesc = ""

            # 메뉴 가격
            try:
                price_el = menu_el.find_element(By.CSS_SELECTOR, "div.GXS1X")
                price_str = price_el.get_attribute("textContent") or "(가격정보 없음)"
                menu_dto.menuPrice = price_str.strip()
            except NoSuchElementException:
                menu_dto.menuPrice = ""

            # 메뉴 이미지
            try:
                img_el = menu_el.find_element(By.CSS_SELECTOR, "div.place_thumb img")
                img_src = img_el.get_attribute("src") or img_el.get_attribute("data-src")
                menu_dto.menuImage = img_src if img_src else "(이미지 없음)"
            except NoSuchElementException:
                menu_dto.menuImage = ""

            menu_list.append(menu_dto)

        dto.menus = menu_list

    except NoSuchElementException:
        print("메뉴 탭은 있으나, 메뉴 정보가 없거나 구조가 다릅니다.")


def crawl_store_details_by_address(address: str) -> List[RestaurantCrawlingStoreDto]:
    """
    1) 네이버 지도 접속 후, address로 검색
    2) 검색결과 첫 매장(들)을 클릭해 상세정보/메뉴 크롤링
    3) RestaurantCrawlingStoreDto 리스트 반환
    """
    result_list: List[RestaurantCrawlingStoreDto] = []
    driver = create_webdriver(headless=False)

    try:
        driver.get("https://map.naver.com/")
        wait = WebDriverWait(driver, 10)

        # 검색창 로딩 대기 후 검색어 입력
        search_box = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input.input_search")))
        search_box.clear()
        search_box.send_keys(address)
        search_box.send_keys(Keys.ENTER)

        # 검색결과 iframe으로 전환
        wait_and_switch_to_frame(driver, "iframe#searchIframe", timeout=10)

        # 검색 결과 목록 로딩 대기
        shop_links = wait.until(
            EC.presence_of_all_elements_located(
                (By.CSS_SELECTOR, "div>.place_bluelink>.TYaxT, div.place_bluelink>span.YwYLL")
            )
        )

        # 최대 1개만 가져온다고 가정(필요 시 수정)
        max_count = min(1, len(shop_links))
        for i in range(max_count):
            shop = shop_links[i]
            # 상세페이지 이동
            click_element_with_wait(driver, shop, timeout=3)

            # 상세페이지 URL이 "/place/"를 포함할 때까지 대기
            short_wait = WebDriverWait(driver, 3)
            short_wait.until(lambda d: "/place/" in d.current_url)

            # 메인 문서로 돌아온 뒤 entryIframe에 다시 진입
            driver.switch_to.default_content()
            wait_and_switch_to_frame(driver, "iframe#entryIframe", timeout=10)

            dto = RestaurantCrawlingStoreDto()
            # (1) 정보 추출
            extract_store_info(driver, wait, dto)
            # (2) 메뉴 추출
            extract_menu_info(driver, wait, dto)

            # 결과 리스트에 추가
            result_list.append(dto)

            # 다시 검색 결과 iframe로 복귀 (다음 매장 크롤링용)
            driver.switch_to.default_content()
            wait_and_switch_to_frame(driver, "iframe#searchIframe", timeout=10)

    except Exception as e:
        print("[ERROR]", e)
    finally:
        driver.quit()

    return result_list


def convert_dto_to_dict(dto: RestaurantCrawlingStoreDto) -> dict:
    """
    RestaurantCrawlingStoreDto 객체를 지정된 JSON 형태의 dict로 변환
    """
    return {
        "id": None,  # 사용 필요에 맞게 설정
        "place_name": dto.placeName or "",
        "main_image_url": dto.mainImageUrl or "",
        "address_name": dto.addressName or "",
        "position": {
            "lat": dto.position_lat if dto.position_lat else 0.0,
            "lng": dto.position_lng if dto.position_lng else 0.0
        },
        "store_info": dto.storeInfo or "",
        "menus": [
            {
                "menu_name": menu.menuName or "",
                "menu_desc": menu.menuDesc or "",
                "menu_price": menu.menuPrice or "",
                "menu_image": menu.menuImage or ""
            }
            for menu in dto.menus
        ]
    }


if __name__ == "__main__":
    keyword = "하단끝집 하단"
    start_time = time.time()

    results = crawl_store_details_by_address(keyword)

    # DTO -> dict -> JSON 변환
    # 여러 가게가 있을 수 있으므로 리스트 형태
    dict_list = [convert_dto_to_dict(store) for store in results]
    json_result = json.dumps(dict_list, ensure_ascii=False, indent=4)

    print(json_result)

    end_time = time.time()
    print(f"\n크롤링에 걸린 시간: {end_time - start_time:.2f}초")

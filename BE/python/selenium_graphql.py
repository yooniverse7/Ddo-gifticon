import json
import re
import time
import random
import requests
from fake_useragent import UserAgent

import asyncio
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


# ============================================
# 1) 네이버 지도 allSearch + GraphQL (비공식)
#    (사용자 제공 코드 #1)
# ============================================

def get_search_result(query: str) -> dict:
    """
    네이버 지도 'allSearch' (비공식) API를 호출해,
    예: "하단끝집"으로 검색 시 JSON 결과를 받아온다.
    """
    url = "https://map.naver.com/p/api/search/allSearch"

    cookies = {
        'NNB': 'R3LR2TRSDV5WO',
        # ... 필요시 추가
    }

    # fake_useragent로 랜덤 User-Agent 생성
    ua = UserAgent()

    headers = {
        'accept': 'application/json, text/plain, */*',
        'accept-language': 'ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4',
        'cache-control': 'no-cache',
        'expires': 'Sat, 01 Jan 2000 00:00:00 GMT',
        'pragma': 'no-cache',
        'priority': 'u=1, i',
        'referer': 'https://map.naver.com',
        'sec-ch-ua': '"Chromium";v="134", "Not:A-Brand";v="24", "Google Chrome";v="134"',
        'sec-ch-ua-mobile': '?0',
        'sec-ch-ua-platform': '"Windows"',
        'sec-fetch-dest': 'empty',
        'sec-fetch-mode': 'cors',
        'sec-fetch-site': 'same-origin',
        'user-agent': ua.random,  # 매 호출마다 다른 UA
    }

    params = {
        'query': query,
        'type': 'all',
        'searchCoord': '129.0680439000028;35.154616899999',
        'boundary': '',
    }

    time.sleep(random.uniform(1, 3))  # (선택) 지연

    resp = requests.get(url, params=params, cookies=cookies, headers=headers)
    resp.raise_for_status()
    return resp.json()

def parse_place_info(data: dict) -> dict:
    """
    allSearch 응답 JSON(data)에서 placeId, 기본 정보(이름, 주소, 좌표 등) 추출
    """
    result_dict = {
        "place_id": None,
        "res_name": None,
        "res_address": None,
        "res_lat": None,
        "res_lng": None,
        "res_image": None,
        "star_rating": None,
        "menu_str": None
    }

    place_data = data.get("result", {}).get("place", {})
    boundary = place_data.get("boundary", [])
    if len(boundary) >= 2:
        result_dict["res_lng"] = boundary[0]  # 경도
        result_dict["res_lat"] = boundary[1]  # 위도

    place_list = place_data.get("list", [])
    if not place_list:
        return result_dict  # 검색 결과 없음

    first_place = place_list[0]
    result_dict["place_id"] = first_place.get("id")
    result_dict["res_name"] = first_place.get("display")  # ex) "<b>하단끝집</b>"
    result_dict["res_address"] = first_place.get("address")

    # 대표이미지(있으면)
    thum_urls = first_place.get("thumUrls", [])
    if thum_urls:
        result_dict["res_image"] = thum_urls[0]

    # 리뷰수
    result_dict["star_rating"] = first_place.get("reviewCount")
    # 메뉴 문자열(짧은 소개)
    result_dict["menu_str"] = first_place.get("menuInfo", None)

    return result_dict


def call_graphql_for_detail(place_id: str) -> dict:
    """
    place.naver.com/graphql (or pcmap-api.place.naver.com/graphql) 비공식 호출
    현재 예시는 announcements만 가져오지만,
    실제론 menus, reviewStats 등등 여러 operationName을 넣을 수 있음
    """
    url = "https://pcmap-api.place.naver.com/graphql"
    ua = UserAgent()

    cookies = {
        'NNB': 'R3LR2TRSDV5WO',
        # ...
    }

    headers = {
        'accept': '*/*',
        'accept-language': 'ko',
        'content-type': 'application/json',
        'origin': 'https://pcmap.place.naver.com',
        'priority': 'u=1, i',
        'referer': f'https://pcmap.place.naver.com/restaurant/{place_id}/home?from=map&locale=ko',
        'sec-ch-ua': '"Chromium";v="134", "Not:A-Brand";v="24", "Google Chrome";v="134"',
        'sec-ch-ua-mobile': '?0',
        'sec-ch-ua-platform': '"Windows"',
        'sec-fetch-dest': 'empty',
        'sec-fetch-mode': 'cors',
        'sec-fetch-site': 'same-site',
        'user-agent': ua.random,
    }

    json_data = [
        {
            "operationName": "getAnnouncements",
            "variables": {
                "businessId": place_id,
                "businessType": "restaurant",
                "deviceType": "pcmap"
            },
            "query": """query getAnnouncements($businessId: String!, $businessType: String!, $deviceType: String!) {
  announcements: announcementsViaCP0(
    businessId: $businessId
    businessType: $businessType
    deviceType: $deviceType
  ) {
    feedId
    title
    period
    thumbnail {
      url
      count
      isVideo
    }
  }
}"""
        },
        # 여기에 "getMenus", "getVisitorReviewStats" 등 필요한 operationName을 추가 가능
    ]

    time.sleep(random.uniform(1, 3))  # (선택) 지연

    resp = requests.post(url, cookies=cookies, headers=headers, json=json_data)
    resp.raise_for_status()
    return resp.json()

def parse_graphql_detail(json_data: dict) -> dict:
    """
    예: getAnnouncements 결과만 파싱
    실제로는 별점, 리뷰수, 메뉴 텍스트 등도 가져오고 싶다면
    여기서 추가 파싱
    """
    if not json_data:
        return {}

    first_resp = json_data[0] if isinstance(json_data, list) else json_data
    data_section = first_resp.get("data", {})
    announcements = data_section.get("announcements", [])

    result = {
        "announcements": []
    }
    for ann in announcements:
        item = {
            "feedId": ann.get("feedId"),
            "title": ann.get("title"),
            "period": ann.get("period"),
            "thumbnailUrl": (ann.get("thumbnail") or {}).get("url")
        }
        result["announcements"].append(item)

    return result


# ============================================
# 2) Selenium으로 메뉴 "이미지" 추출
#    (사용자 제공 코드 #2) 중 필요한 부분만 발췌
# ============================================

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
        chrome_options.add_argument("--headless")

    # 이미지 로딩 차단 (선택)
    chrome_prefs = {
        "profile.default_content_setting_values": {
            "images": 2  # 2: 차단 / 1: 허용
        }
    }
    chrome_options.experimental_options["prefs"] = chrome_prefs

    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-images")
    chrome_options.add_experimental_option("prefs", {'profile.managed_default_content_settings.images': 2})
    chrome_options.add_argument('--blink-settings=imagesEnabled=false')
    chrome_options.add_argument("--window-size=1920,1080")

    # 네트워크 모두 로드 전까지 기다리지 않고 partial 로딩할 수도 있음
    # chrome_options.page_load_strategy = "none"

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


def extract_menu_info(driver: webdriver.Chrome, wait: WebDriverWait, dto: RestaurantCrawlingStoreDto):
    """
    '메뉴' 탭 클릭 후, 각 메뉴 정보(특히 이미지)를 추출
    사용자가 준 코드 그대로 사용
    """
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

            # 메뉴 설명
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
                menu_dto.menuImage = img_src if img_src else ""
            except NoSuchElementException:
                menu_dto.menuImage = ""

            menu_list.append(menu_dto)

        dto.menus = menu_list

    except NoSuchElementException:
        print("메뉴 탭은 있으나, 메뉴 정보가 없거나 구조가 다릅니다.")


def crawl_store_details_by_keyword(keyword: str) -> List[RestaurantCrawlingStoreDto]:
    """
    1) 네이버 지도 접속 후, address 로 검색
    2) 검색결과 첫 매장을 클릭해 상세 -> 메뉴탭 크롤링
    3) RestaurantCrawlingStoreDto 리스트로 반환
    """
    result_list: List[RestaurantCrawlingStoreDto] = []
    driver = create_webdriver(headless=False)

    try:
        driver.get("https://map.naver.com/")
        wait = WebDriverWait(driver, 10)

        # 검색창 로딩 대기 후 검색어 입력
        search_box = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input.input_search")))
        search_box.clear()
        search_box.send_keys(keyword)
        search_box.send_keys(Keys.ENTER)

        # 검색결과 iframe으로 전환
        wait_and_switch_to_frame(driver, "iframe#searchIframe", timeout=10)

        # 검색 결과 목록 로딩 대기
        shop_links = wait.until(
            EC.presence_of_all_elements_located(
                (By.CSS_SELECTOR, "div>.place_bluelink>.TYaxT, div.place_bluelink>span.YwYLL")
            )
        )

        # 일단 첫 번째 매장만 예시로 크롤링
        if shop_links:
            shop = shop_links[0]
            click_element_with_wait(driver, shop, timeout=3)

            # 상세페이지 URL이 "/place/"를 포함할 때까지 짧게 대기
            short_wait = WebDriverWait(driver, 3)
            short_wait.until(lambda d: "/place/" in d.current_url)

            # 상세페이지 iframe
            driver.switch_to.default_content()
            wait_and_switch_to_frame(driver, "iframe#entryIframe", timeout=10)

            dto = RestaurantCrawlingStoreDto()

            # 가게명
            try:
                name_el = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "span.GHAhO")))
                dto.placeName = name_el.text.strip()
            except:
                dto.placeName = ""

            # 메뉴 정보(여기서 이미지 포함)
            extract_menu_info(driver, wait, dto)
            result_list.append(dto)

    except Exception as e:
        print("[ERROR in crawl_store_details_by_address]", e)
    finally:
        driver.quit()

    return result_list


# ============================================
# 3) 비동기로 실행하기 위한 래핑
#    (GraphQL, Selenium 병렬 처리)
# ============================================

async def async_call_graphql(place_id: str) -> dict:
    """
    그래프QL 호출은 동기 함수를 그대로 사용하되,
    asyncio의 run_in_executor 로 별도 스레드에서 실행해 비동기처럼 처리
    """
    loop = asyncio.get_running_loop()
    return await loop.run_in_executor(None, call_graphql_for_detail, place_id)


async def async_selenium_crawl(keyword: str) -> List[RestaurantCrawlingStoreDto]:
    """
    Selenium 코드도 동기이므로, run_in_executor로 실행
    """
    loop = asyncio.get_running_loop()
    return await loop.run_in_executor(None, crawl_store_details_by_keyword, keyword)


# ============================================
# 4) 최종 main (비동기)
# ============================================

async def main():
    total_start = time.time()

    query = "쏘렌토"
    print(f"[INFO] 검색어(query) = {query}")

    # (A) allSearch 호출 (동기)
    start_a = time.time()
    search_data = get_search_result(query)
    place_info = parse_place_info(search_data)
    end_a = time.time()
    print(f"[TIME] (A) allSearch 처리 시간: {end_a - start_a:.3f}초")

    place_id = place_info.get("place_id")
    if not place_id:
        print("[ERROR] place_id를 찾을 수 없습니다. 검색 결과 없음")
        return

    print(f"[INFO] place_id = {place_id}")
    print(f"[INFO] 매장 주소 = {place_info.get('res_address')}")

    # (B) 그래프QL과 Selenium을 비동기로 병렬 실행
    #     Selenium은 "주소"로 검색 -> 첫 매장의 메뉴 이미지 등을 추출
    #     GraphQL은 place_id 기반으로 공지/별점 등 텍스트 정보 추출
    start_b = time.time()

    graphql_task = asyncio.create_task(async_call_graphql(place_id))
    # 여기서는 place_info["res_address"]를 넣어서 메뉴탭 크롤링
    store_name_cleaned = re.sub(r"<.*?>", "", place_info["res_name"] or "")
    selenium_task = asyncio.create_task(async_selenium_crawl(store_name_cleaned))

    gql_data, store_list = await asyncio.gather(graphql_task, selenium_task)
    end_b = time.time()
    print(f"[TIME] (B) GraphQL + Selenium 병렬 처리 시간: {end_b - start_b:.3f}초")

    # (C) 그래프QL 파싱
    detail_info = parse_graphql_detail(gql_data)

    # (D) Selenium 결과에서 메뉴 정보
    #     - 예시상 store_list[0] 에 menus가 들어있을 것
    selenium_menus = []
    if store_list:
        dto = store_list[0]
        selenium_menus = dto.menus  # List[RestaurantCrawlingMenuDto]

    # (E) 최종 병합
    # place_info: allSearch 결과
    # detail_info: announcements (그래프QL)
    # selenium_menus: 메뉴 + 이미지
    combined = {
        "res_name": re.sub(r"<.*?>", "", place_info["res_name"] or ""),  # <b> 태그 제거
        "res_address": place_info["res_address"],
        "res_lat": place_info["res_lat"],
        "res_lng": place_info["res_lng"],
        "res_image": place_info["res_image"],
        "review_count": place_info["star_rating"],  # allSearch에서의 리뷰수
        "announcements": detail_info.get("announcements", []),
        "selenium_menus": [
            {
                "menu_name": m.menuName,
                "menu_desc": m.menuDesc,
                "menu_price": m.menuPrice,
                "menu_image": m.menuImage
            } for m in selenium_menus
        ]
    }

    # 출력
    print("\n===== 최종 병합 결과 =====")
    print(json.dumps(combined, ensure_ascii=False, indent=4))

    total_end = time.time()
    print(f"\n[TIME] 전체 실행 시간: {total_end - total_start:.3f}초")


# ============================================
# 실행
# ============================================
if __name__ == "__main__":
    asyncio.run(main())

import time
from typing import List
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

def create_webdriver(headless: bool = True) -> webdriver.Chrome:
    chrome_options = Options()
    if headless:
        chrome_options.add_argument("--headless=new")  # 크롬109+ 권장 옵션

    # 이미지 로딩 차단
    chrome_prefs = {"profile.default_content_setting_values": {"images": 2}}
    chrome_options.experimental_options["prefs"] = chrome_prefs

    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--window-size=1920,1080")

    chrome_options.add_argument(
        "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"
    )

    driver = webdriver.Chrome(
        service=Service(ChromeDriverManager().install()),
        options=chrome_options
    )
    return driver

def crawl_menu_by_place_id(place_id: str) -> List[dict]:
    """
    place_id로 구성된 URL로 바로 이동한 뒤, 메뉴 탭을 파싱하여
    [ {menuName, menuImage, ...}, ... ] 형태의 리스트를 반환.
    """

    url = f"https://map.naver.com/p/entry/place/{place_id}?c=15.00,0,0,0,dh&placePath=/menu"
    driver = create_webdriver(headless=True)  # 필요 시 headless=False
    wait = WebDriverWait(driver, 6)  # 최대 6초까지 대기

    menu_list = []

    try:
        driver.get(url)

        # (1) iFrame 등장 대기 후 진입 (CSS Selector 대신 BY.CSS_SELECTOR 그대로 사용 가능)
        wait.until(
            EC.frame_to_be_available_and_switch_to_it((By.CSS_SELECTOR, "iframe#entryIframe"))
        )

        # (2) li.E2jtL 요소를 XPath로 대체
        # 예: "//li[contains(@class,'E2jtL')]"
        # presence_of_all_elements_located로 대기
        menu_elements = wait.until(
            EC.presence_of_all_elements_located(
                (By.XPATH, "//li[contains(@class,'E2jtL')]")
            )
        )

        # (3) 각 메뉴 아이템 파싱
        for menu_el in menu_elements:
            item = {
                "menuName": "(이름 없음)",
                "menuImage": "(이미지 없음)",
            }

            # (A) 메뉴 이름: CSS "span.lPzHi" → XPath로
            # 예시: ".//span[contains(@class,'lPzHi')]"
            try:
                name_el = menu_el.find_element(By.XPATH, ".//span[contains(@class,'lPzHi')]")
                item["menuName"] = name_el.text.strip()
            except NoSuchElementException:
                pass

            # (B) 메뉴 이미지: CSS "div.place_thumb img" → XPath
            # 예시: ".//div[contains(@class,'place_thumb')]//img"
            try:
                img_el = menu_el.find_element(By.XPATH, ".//div[contains(@class,'place_thumb')]//img")
                img_src = img_el.get_attribute("src") or img_el.get_attribute("data-src")
                item["menuImage"] = img_src if img_src else "(이미지 없음)"
            except NoSuchElementException:
                pass

            menu_list.append(item)

    except TimeoutException:
        print("[ERROR] 시간 내에 메뉴 요소를 찾지 못했습니다.")
    except Exception as e:
        print("[ERROR]", e)
    finally:
        driver.quit()

    return menu_list

if __name__ == "__main__":
    place_id = "36123974"
    # 36123974
    start_time = time.time()

    result = crawl_menu_by_place_id(place_id)
    print("===== 메뉴 목록 =====")
    for idx, menu in enumerate(result, start=1):
        print(f"{idx}. 메뉴명={menu['menuName']} / 이미지={menu['menuImage']}")

    end_time = time.time()
    print(f"\n크롤링에 걸린 시간: {end_time - start_time:.2f}초")

import requests
import json

def get_menu_list_by_place_id(place_id: str) -> list:
    """
    - placeId(예: "1607569345")를 받아,
    - 네이버 지도 비공개 REST API(또는 GraphQL)로 직접 요청을 보낸 뒤
    - 메뉴 정보를 JSON 형태로 파싱 → [ {menuName, menuPrice, menuImage, ...}, ... ] 반환
    """

    # 1) 미리 DevTools로 찾은 '메뉴 API'의 URL (예: pcmap-api.place.naver.com/restaurant/{placeId}/menus) 
    #    => 실제 URL과 파라미터는 DevTools에서 "Copy as cURL"로 얻어야 함
    url = f"https://pcmap-api.place.naver.com/restaurant/{place_id}/menus?caller=pcweb&...타유관파라미터..."

    # 2) DevTools에서 "Copy as cURL" → python 변환한 결과에 나온 쿠키/헤더를 그대로 복사
    headers = {
        "User-Agent": "Mozilla/5.0 ...", 
        "Accept": "application/json",
        "Referer": f"https://pcmap.place.naver.com/restaurant/{place_id}/menu/list?from=map&...",  
        # 필요하면 추가 헤더 / 쿠키
    }
    cookies = {
        "NNB": "...",
        "NID_AUT": "...",
        # ...
    }

    resp = requests.get(url, headers=headers, cookies=cookies, timeout=5)
    if resp.status_code != 200:
        print("[ERROR]", resp.status_code)
        return []

    # 3) JSON 응답 파싱
    try:
        data = resp.json()
    except json.JSONDecodeError:
        print("[ERROR] JSON Decode failed")
        return []

    # 4) 응답 구조에 따라 메뉴 정보 꺼내기
    #    예: data["menus"] 가 [{ "menuName":..., "price":..., "image":... }, ...]
    #    실제 key 이름은 DevTools에서 응답을 확인해야 합니다.
    menus_raw = data.get("menus", [])
    
    menu_list = []
    for m in menus_raw:
        item = {
            "menuName": m.get("menuName", "(이름없음)"),
            "menuPrice": m.get("menuPrice", "0"),
            "menuImage": m.get("menuImage", None),
        }
        menu_list.append(item)

    return menu_list


if __name__ == "__main__":
    place_id = "1607569345"  # 예시
    menu_data = get_menu_list_by_place_id(place_id)

    print("===== 메뉴 목록 =====")
    for idx, menu in enumerate(menu_data, start=1):
        print(f"{idx}. 이름={menu['menuName']}, 가격={menu['menuPrice']}, 이미지={menu['menuImage']}")

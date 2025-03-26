import json
import re
import time
import random
import requests
from fake_useragent import UserAgent

# ============================================
# 1) 네이버 지도 allSearch 호출 코드
# ============================================

def get_search_result(query: str) -> dict:
    """
    네이버 지도 'allSearch' (비공식) API를 호출해,
    예: "하단끝집"으로 검색 시 JSON 결과를 받아온다.

    - 쿠키/헤더는 DevTools → 'Copy as cURL' 후 변환해서 얻은 값.
    - 'fake_useragent'로 User-Agent를 랜덤하게 사용함.
    """
    url = "https://map.naver.com/p/api/search/allSearch"

    cookies = {
        'NNB': 'R3LR2TRSDV5WO',
        '_ga': 'GA1.2.1013108440.1737076775',
        '_ga_6Z6DP60WFK': 'GS1.2.1737076782.1.0.1737076782.60.0.0',
        'ASID': '3b14c37f00000194ba57f1a400000022',
        'nid_inf': '1919020209',
        'NID_AUT': 'cMGKtiIGGVXCu6OwF/MXkfA8eO0zYBvRlLP6pC1CCx5KNeri6WVPEL9zmywrAzYx',
        'NID_JKL': 'BwMfFY8ZZ82D91aDjHRj4xh0rqDooGxnNlQwLo6+J2Y=',
        'NAC': 'SOUFBcAej4VAB',
        'NACT': '1',
        'SRT30': '1742867569',
        'NID_SES': 'AAABnZlZrolQC0...',
        'SRT5': '1742872695',
        'BUC': 'jpCc9HN2cIFY_N5i...',
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
        'referer': 'https://map.naver.com/p/search/%ED%95%98%EB%8B%A8%EB%81%9D%EC%A7%91?c=15.00,0,0,0,dh',
        'sec-ch-ua': '"Chromium";v="134", "Not:A-Brand";v="24", "Google Chrome";v="134"',
        'sec-ch-ua-mobile': '?0',
        'sec-ch-ua-platform': '"Windows"',
        'sec-fetch-dest': 'empty',
        'sec-fetch-mode': 'cors',
        'sec-fetch-site': 'same-origin',
        'user-agent': ua.random,  # <-- 포인트: 매 호출마다 다른 UA
    }

    params = {
        'query': query,
        'type': 'all',
        'searchCoord': '129.0680439000028;35.154616899999',
        'boundary': '',
    }

    # (선택) 요청 간 랜덤 지연
    time.sleep(random.uniform(1, 3))

    resp = requests.get(url, params=params, cookies=cookies, headers=headers)
    resp.raise_for_status()
    data = resp.json()
    return data


def parse_place_info(data: dict) -> dict:
    """
    allSearch 응답 JSON(data)에서
    placeId(=매장 id), 기본 정보(이름, 주소, 좌표 등)를 추출.
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

    # 대표이미지(여러 개 중 첫 번째)
    thum_urls = first_place.get("thumUrls", [])
    if thum_urls:
        result_dict["res_image"] = thum_urls[0]

    # 별점 대신 reviewCount
    result_dict["star_rating"] = first_place.get("avgRating")
    # 메뉴 문자열
    result_dict["menu_str"] = first_place.get("menuInfo", None)

    return result_dict

# ============================================
# 2) 그래프QL (비공식) + fake_useragent 예시
# ============================================

def call_graphql_for_detail(place_id: str) -> dict:
    """
    DevTools -> place.naver.com/graphql 에서 Copy as cURL
    -> curlconverter로 변환한 내용을 기반으로 작성.

    아래 예시는 하나의 operationName = getAnnouncements 만 넣었지만,
    실제론 여러 operationName이 들어가는 배열일 수도 있음.
    (ex. [ {...}, {...} ])
    """

    url = "https://pcmap-api.place.naver.com/graphql"

    ua = UserAgent()

    cookies = {
        'NNB': 'R3LR2TRSDV5WO',
        # ...
        'BUC': 'H2MBrIgQLpOwmVeAd_...',
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
        'user-agent': ua.random,  # <-- fake UA
        # ...
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
    ...AnnouncementFields
    __typename
  }
}

fragment AnnouncementFields on Feed {
  feedId
  category
  categoryI18n
  title
  relativeCreated
  period
  thumbnail {
    url
    count
    isVideo
    __typename
  }
  __typename
}"""
        },
    ]

    # (선택) 랜덤 지연
    time.sleep(random.uniform(1, 3))

    resp = requests.post(url, cookies=cookies, headers=headers, json=json_data)
    resp.raise_for_status()
    return resp.json()

def parse_graphql_detail(json_data: dict) -> dict:
    """
    간단 예시: "getAnnouncements" 응답 부분만 파싱
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
            "thumbnailUrl": ann.get("thumbnail", {}).get("url")
        }
        result["announcements"].append(item)

    return result


# ============================================
# 3) 통합 실행 + 시간 측정
# ============================================

def main():
    # 전체 실행 시간 시작
    total_start = time.time()

    query = "우연횟집"

    # (A) allSearch 호출
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

    # (B) 그래프QL 상세
    start_b = time.time()
    gql_data = call_graphql_for_detail(place_id)
    detail_info = parse_graphql_detail(gql_data)
    end_b = time.time()
    print(f"[TIME] (B) GraphQL 처리 시간: {end_b - start_b:.3f}초")

    # (C) 데이터 결합
    start_c = time.time()
    combined = {
        "res_name": re.sub(r"<.*?>", "", place_info["res_name"] or ""),  # <b> 태그 제거
        "res_address": place_info["res_address"],
        "res_lat": place_info["res_lat"],
        "res_lng": place_info["res_lng"],
        "res_image": place_info["res_image"],
        "star_rating": place_info["star_rating"],
        "menu_info_str": place_info["menu_str"],  # allSearch의 menuInfo
        "announcements": detail_info.get("announcements", [])
    }
    end_c = time.time()
    print(f"[TIME] (C) 데이터 결합 시간: {end_c - start_c:.3f}초")

    # 결과 출력
    print("\n===== 최종 결과 =====")
    print(f"매장명: {combined['res_name']}")
    print(f"주소: {combined['res_address']}")
    print(f"위도,경도: ({combined['res_lat']}, {combined['res_lng']})")
    print(f"대표이미지: {combined['res_image']}")
    print(f"리뷰수: {combined['star_rating']}")
    print(f"메뉴(문자열): {combined['menu_info_str']}")
    print("\n[GraphQL Announcements]")
    for idx, ann in enumerate(combined["announcements"], start=1):
        print(f" ({idx}) feedId={ann['feedId']} / title={ann['title']} / period={ann['period']} / thumb={ann['thumbnailUrl']}")

    # 전체 실행 시간 종료
    total_end = time.time()
    print(f"\n[TIME] 전체 실행 시간: {total_end - total_start:.3f}초")


if __name__ == "__main__":
    main()
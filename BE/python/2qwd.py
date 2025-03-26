import json
import re
import time
import random
import requests
from fake_useragent import UserAgent


# ============================================
# 1) 네이버 지도 allSearch (비공식) 호출
# ============================================

def get_search_result(query: str) -> dict:
    """
    네이버 지도 allSearch 비공식 API를 호출하여 JSON 결과를 가져온다.
    ex) query = "하단끝집" → 검색 결과 JSON

    Returns:
        dict: allSearch API의 JSON 응답
    """
    url = "https://map.naver.com/p/api/search/allSearch"

    # 필요한 쿠키들 (DevTools → Copy as cURL 결과 기반)
    cookies = {
        'NNB': 'R3LR2TRSDV5WO',
        '_ga': 'GA1.2.1013108440.1737076775',
        # ... (필요에 따라 추가)
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
        # 검색어 한글 문제를 피하기 위해 간단하게 처리할 수도 있음
        'referer': f'https://map.naver.com/p/search/{query}?c=15.00,0,0,0,dh',
        'sec-ch-ua': '"Chromium";v="134", "Not:A-Brand";v="24", "Google Chrome";v="134"',
        'sec-ch-ua-mobile': '?0',
        'sec-ch-ua-platform': '"Windows"',
        'sec-fetch-dest': 'empty',
        'sec-fetch-mode': 'cors',
        'sec-fetch-site': 'same-origin',
        'user-agent': ua.random,  # 매 호출마다 다른 User-Agent
    }

    params = {
        'query': query,
        'type': 'all',
        # 아래 좌표/파라미터는 사용 상황에 맞게 수정 가능
        'searchCoord': '129.0680439000028;35.154616899999',
        'boundary': '',
    }

    # (선택) 요청 간 짧은 랜덤 지연
    time.sleep(random.uniform(1, 3))

    resp = requests.get(url, params=params, cookies=cookies, headers=headers)
    resp.raise_for_status()
    return resp.json()


def parse_place_info(data: dict) -> dict:
    """
    allSearch 응답(JSON)에서 place_id, 매장명, 주소, 좌표, 대표이미지 등을 추출.

    Args:
        data (dict): allSearch API의 JSON 결과

    Returns:
        dict: {
            "place_id": str or None,
            "res_name": str or None,
            "res_address": str or None,
            "res_lat": float or None,
            "res_lng": float or None,
            "res_image": str or None,
            "star_rating": None or number,
            "menu_str": str or None
        }
    """
    result_dict = {
        "place_id": None,
        "res_name": None,
        "res_address": None,
        "res_lat": None,
        "res_lng": None,
        "res_image": None,
        "star_rating": None,  # 여기서는 avgRating/reviewCount 대신 None
        "menu_str": None
    }

    place_data = data.get("result", {}).get("place", {})
    boundary = place_data.get("boundary", [])

    # boundary = [경도, 위도, ...] 형태일 때
    if len(boundary) >= 2:
        result_dict["res_lng"] = boundary[0]
        result_dict["res_lat"] = boundary[1]

    place_list = place_data.get("list", [])
    if not place_list:
        return result_dict  # 검색 결과가 없는 경우

    first_place = place_list[0]
    result_dict["place_id"] = first_place.get("id")
    # <b>태그가 들어갈 수 있어서, 실제로는 나중에 re.sub로 제거
    result_dict["res_name"] = first_place.get("display")
    result_dict["res_address"] = first_place.get("address")

    thum_urls = first_place.get("thumUrls", [])
    if thum_urls:
        result_dict["res_image"] = thum_urls[0]

    # allSearch에서 "avgRating" or "reviewCount"가 있으면 여기에 할당할 수도 있으나
    # 여기서는 star_rating=None로 유지하거나 필요시 reviewCount를 할당 가능
    # result_dict["star_rating"] = first_place.get("reviewCount")

    # 간단 메뉴 문자열
    result_dict["menu_str"] = first_place.get("menuInfo", None)

    return result_dict


# ============================================
# 2) 그래프QL (비공식) + fake_useragent
# ============================================

def call_graphql_for_detail(place_id: str) -> dict:
    """
    place.naver.com/graphql 비공식 API 호출 예시.
    현재 예시는 'getAnnouncements'만 호출.
    별점/리뷰수를 원한다면 'getVisitorReviewStats' 등 추가해야 함.

    Args:
        place_id (str): 매장 id (문자열)

    Returns:
        dict: GraphQL 응답 (배열 or 딕셔너리)
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

    # 현재는 announcements만 요청
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

    time.sleep(random.uniform(1, 3))

    resp = requests.post(url, cookies=cookies, headers=headers, json=json_data)
    resp.raise_for_status()
    return resp.json()


def parse_graphql_detail(json_data: list) -> dict:
    """
    json_data가 [ {...}, {...}, ... ] 형태라고 했을 때,
    - 각 원소의 data.visitorReviewStats 부분에 review.avgRating 이 있으면 별점으로 사용
    - review.totalCount 를 리뷰 개수로 사용
    """
    if not json_data:
        return {}

    star_rating = None
    review_count = None

    for obj in json_data:
        data_section = obj.get("data", {})

        # visitorReviewStats가 있나?
        if "visitorReviewStats" in data_section:
            vrs = data_section["visitorReviewStats"] or {}
            review_info = vrs.get("review", {})
            # 별점
            if "avgRating" in review_info:
                star_rating = review_info["avgRating"]
            # 리뷰 수
            if "totalCount" in review_info:
                review_count = review_info["totalCount"]

    return {
        "star_rating": star_rating,
        "review_count": review_count
    }



# ============================================
# 3) 메인 실행 + 시간 측정
# ============================================

def main():
    """
    1) allSearch로 place_id 등 기본정보 파싱
    2) GraphQL 호출(getAnnouncements)
    3) 결합 후 결과 출력
    """
    total_start = time.time()
    query = "쏘렌토"

    # (A) allSearch
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

    # (B) GraphQL
    start_b = time.time()
    # gql_data = call_graphql_for_detail(place_id)
    # detail_info = parse_graphql_detail(gql_data)
    end_b = time.time()
    print(f"[TIME] (B) GraphQL 처리 시간: {end_b - start_b:.3f}초")

    # (C) 결합
    start_c = time.time()
    # star_rating은 allSearch에서 first_place.get("avgRating")를 가져오도록 되어 있으나
    # 현재 parse_place_info()에선 별점 로직이 없으므로 None
    combined = {
        "res_name": re.sub(r"<.*?>", "", place_info["res_name"] or ""),
        "res_address": place_info["res_address"],
        "res_lat": place_info["res_lat"],
        "res_lng": place_info["res_lng"],
        "res_image": place_info["res_image"],
        "star_rating": place_info["star_rating"],  # 현재 None
        "menu_info_str": place_info["menu_str"],
        # "announcements": detail_info.get("announcements", [])
    }
    end_c = time.time()
    print(f"[TIME] (C) 데이터 결합 시간: {end_c - start_c:.3f}초")

    # 출력
    print("\n===== 최종 결과 =====")
    print(f"매장명: {combined['res_name']}")
    print(f"주소: {combined['res_address']}")
    print(f"위도,경도: ({combined['res_lat']}, {combined['res_lng']})")
    print(f"대표이미지: {combined['res_image']}")
    print(f"리뷰수(별점X): {combined['star_rating']}")
    print(f"메뉴(문자열): {combined['menu_info_str']}")
    parsed_result = parse_graphql_detail(json_data)
    print(parsed_result["star_rating"], parsed_result["review_count"])

    print("\n[GraphQL Announcements]")
    for idx, ann in enumerate(combined["announcements"], start=1):
        print(f" ({idx}) feedId={ann['feedId']} / title={ann['title']} / period={ann['period']} / thumb={ann['thumbnailUrl']}")

    total_end = time.time()
    print(f"\n[TIME] 전체 실행 시간: {total_end - total_start:.3f}초")


if __name__ == "__main__":
    main()

package com.example.ddo_pay.restaurant.service.crawling;

import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.response.ResponsePositionDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingMenuDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingStoreDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NaverCrawlingServiceImpl implements NaverCrawlingService {

	private final RedisTemplate<String, String> redisTemplate;
	private final OkHttpClient httpClient = new OkHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public NaverCrawlingServiceImpl(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * allSearch와 GraphQL API를 호출하여 매장 기본 정보를 추출하고,
	 * 최종 JSON 구조에 맞는 DTO를 구성합니다.
	 *
	 * 최종 결과는 아래와 같이 구성됩니다:
	 *
	 * - place_name: HTML 태그 제거된 매장명 (res_name)
	 * - main_image_url: 대표 이미지 URL (res_image)
	 * - address_name: 매장 주소 (res_address)
	 * - position: { lat, lng } (boundary 값을 이용)
	 * - place_id: 매장 고유 ID
	 * - menus: menuInfo 문자열을 파싱하여 생성된 메뉴 목록 (각 메뉴는 menu_name, menu_price 포함)
	 */
	@Override
	public List<RestaurantCrawlingStoreDto> getCrawlingInfo(RestaurantCrawlingRequestDto requestDto) {
		// 1) 요청 데이터 결합: placeName과 addressName
		String combinedQuery = requestDto.getPlaceName() + " " + requestDto.getAddressName();

		// 2) allSearch API 호출 및 파싱
		Map<String, Object> allSearchData = callAllSearch(combinedQuery);
		String placeId = (String) allSearchData.get("place_id");
		Double resLat = (Double) allSearchData.get("res_lat");
		Double resLng = (Double) allSearchData.get("res_lng");

		log.info("[allSearch] place_id={}, res_lat={}, res_lng={}", placeId, resLat, resLng);

		// 3) DTO 구성 (웹 스크래핑 부분을 제거하고 API 응답으로 대체)
		RestaurantCrawlingStoreDto dto = new RestaurantCrawlingStoreDto();
		// HTML 태그 제거 후, place_name에 설정
		String rawName = (String) allSearchData.get("res_name");
		String placeName = rawName != null ? rawName.replaceAll("<.*?>", "") : "";
		dto.setPlaceName(placeName);
		dto.setMainImageUrl((String) allSearchData.get("res_image"));
		dto.setAddressName((String) allSearchData.get("res_address"));
		dto.setPlaceId(placeId);
		// 별점: 문자열을 BigDecimal로 변환 (없으면 0)
		String starRatingStr = (String) allSearchData.get("star_rating");
		try {
			dto.setStarRating(starRatingStr != null ? new BigDecimal(starRatingStr) : BigDecimal.ZERO);
		} catch (Exception e) {
			dto.setStarRating(BigDecimal.ZERO);
		}
		// 위치 정보 설정
		if (resLat != null && resLng != null) {
			ResponsePositionDto pos = new ResponsePositionDto();
			pos.setLat(resLat);
			pos.setLng(resLng);
			dto.setPosition(pos);
		}
		// 메뉴 파싱: menuInfo 문자열을 "|" 구분자로 나누어 메뉴 목록 생성
		String menuStr = (String) allSearchData.get("menu_str");
		dto.setMenus(parseMenus(menuStr));

		// 4) GraphQL API 호출 (추가 정보 확인용 - 결과는 로그로 출력)
		if (placeId != null) {
			JsonNode gqlNode = callGraphqlForDetail(placeId);
			log.info("GraphQL node: {}", gqlNode.toPrettyString());
		}

		List<RestaurantCrawlingStoreDto> results = new ArrayList<>();
		results.add(dto);
		return results;
	}

	@Override
	public List<RestaurantCrawlingStoreDto> getOrLoadCrawlingResult(RestaurantCrawlingRequestDto req) {
		String combinedQuery = req.getPlaceName() + " " + req.getAddressName();
		Map<String, Object> allSearch = callAllSearch(combinedQuery);
		String placeId = (String) allSearch.get("place_id");

		if (placeId != null && !placeId.isBlank()) {
			String redisKey = "crawling:" + placeId;
			String cachedJson = redisTemplate.opsForValue().get(redisKey);
			if (cachedJson != null) {
				try {
					return objectMapper.readValue(cachedJson, new TypeReference<List<RestaurantCrawlingStoreDto>>() {});
				} catch (Exception e) {
					log.warn("캐시 역직렬화 실패 → 새 크롤링 진행", e);
				}
			}
		}

		List<RestaurantCrawlingStoreDto> result = this.getCrawlingInfo(req);
		if (placeId != null && !placeId.isBlank() && !result.isEmpty()) {
			String redisKey = "crawling:" + placeId;
			try {
				String toCache = objectMapper.writeValueAsString(result);
				redisTemplate.opsForValue().set(redisKey, toCache, Duration.ofHours(4));
			} catch (Exception e) {
				log.warn("캐시 직렬화 실패", e);
			}
		}
		return result;
	}

	// ====== AllSearch API 호출 및 파싱 ======
	private Map<String, Object> callAllSearch(String query) {
		Map<String, Object> result = new HashMap<>();
		// 결과 초기값 설정 (null)
		result.put("placeId", null);
		result.put("res_lat", null);
		result.put("res_lng", null);
		result.put("res_name", null);
		result.put("res_address", null);
		result.put("res_image", null);
		result.put("star_rating", null);
		result.put("menu_str", null);

		try {
			// URL 생성 (검색어, type, 좌표, boundary 등)
			HttpUrl url = HttpUrl.parse("https://map.naver.com/p/api/search/allSearch")
				.newBuilder()
				.addQueryParameter("query", query)
				.addQueryParameter("type", "all")
				.addQueryParameter("searchCoord", "129.0680439000028;35.154616899999")
				.addQueryParameter("boundary", "")
				.build();

			// 한글 등 특수문자가 Referer에 포함되지 않도록 URL 인코딩
			String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

			// Request 헤더 구성: Python 코드에서 사용한 헤더를 참고
			Request request = new Request.Builder()
				.url(url)
				.header("Accept", "application/json, text/plain, */*")
				.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
				.header("Origin", "https://map.naver.com")
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
				// 실제 브라우저에서 사용하는 쿠키 값으로 교체 필요 (예시로 일부 값만 사용)
				.header("Cookie", "NNB=R3LR2TRSDV5WO; NID_AUT=your_actual_cookie_value;")
				// Referer: 검색어를 포함한 URL (필요에 따라 구체화)
				.header("Referer", "https://map.naver.com/p/search/" + encodedQuery + "?c=15.00,0,0,0,dh&isCorrectAnswer=true")
				.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					log.warn("allSearch 호출 실패: {}", response);
					return result;
				}
				String bodyStr = response.body().string();
				JsonNode root = objectMapper.readTree(bodyStr);

				// 위치 정보 추출 (boundary 배열)
				JsonNode boundary = root.path("result").path("place").path("boundary");
				if (boundary.isArray() && boundary.size() >= 2) {
					double lng = boundary.get(0).asDouble();
					double lat = boundary.get(1).asDouble();
					result.put("res_lat", lat);
					result.put("res_lng", lng);
				}

				// 매장 기본 정보: 첫 번째 매장 항목
				JsonNode listArr = root.path("result").path("place").path("list");
				if (listArr.isArray() && listArr.size() > 0) {
					JsonNode first = listArr.get(0);
					result.put("place_id", first.path("id").asText(null));
					result.put("res_name", first.path("display").asText(null));
					result.put("res_address", first.path("address").asText(null));
					JsonNode thumUrls = first.path("thumUrls");
					if (thumUrls.isArray() && thumUrls.size() > 0) {
						result.put("res_image", thumUrls.get(0).asText(null));
					}
					result.put("star_rating", first.path("avgRating").asText(null));
					result.put("menu_str", first.path("menuInfo").asText(null));
				}
			}
		} catch (Exception e) {
			log.error("callAllSearch error: ", e);
		}
		return result;
	}

	private Map<String, Object> parsePlaceInfo(JsonNode data) {
		Map<String, Object> result = new HashMap<>();
		// 기본값 설정
		result.put("place_id", null);
		result.put("res_name", null);
		result.put("res_address", null);
		result.put("res_lat", null);
		result.put("res_lng", null);
		result.put("res_image", null);
		result.put("star_rating", null);
		result.put("menu_str", null);

		// "result" → "place" 노드 추출
		JsonNode placeData = data.path("result").path("place");

		// boundary 배열: 경도와 위도
		JsonNode boundary = placeData.path("boundary");
		if (boundary.isArray() && boundary.size() >= 2) {
			result.put("res_lng", boundary.get(0).asDouble());
			result.put("res_lat", boundary.get(1).asDouble());
		}

		// 매장 리스트 추출
		JsonNode placeList = placeData.path("list");
		if (!placeList.isArray() || placeList.size() == 0) {
			return result;  // 검색 결과 없음
		}

		// 첫 번째 매장 정보 사용
		JsonNode firstPlace = placeList.get(0);
		result.put("place_id", firstPlace.path("id").asText(null));
		result.put("res_name", firstPlace.path("display").asText(null));
		result.put("res_address", firstPlace.path("address").asText(null));

		// 대표 이미지: thumUrls 배열의 첫 번째 값 사용
		JsonNode thumUrls = firstPlace.path("thumUrls");
		if (thumUrls.isArray() && thumUrls.size() > 0) {
			result.put("res_image", thumUrls.get(0).asText(null));
		}

		result.put("star_rating", firstPlace.path("avgRating").asText(null));
		result.put("menu_str", firstPlace.path("menuInfo").asText(null));

		return result;
	}




	// ====== GraphQL API 호출 ======
	private JsonNode callGraphqlForDetail(String placeId) {
		try {
			String gqlBody = """
                    [
                      {
                        "operationName":"getAnnouncements",
                        "variables":{
                          "businessId":"%s",
                          "businessType":"restaurant",
                          "deviceType":"pcmap"
                        },
                        "query": "query getAnnouncements($businessId: String!, $businessType: String!, $deviceType: String!) { announcements: announcementsViaCP0(businessId: $businessId, businessType: $businessType, deviceType: $deviceType ) { feedId title period thumbnail { url count isVideo } } }"
                      }
                    ]
                    """.formatted(placeId);
			RequestBody reqBody = RequestBody.create(gqlBody, MediaType.parse("application/json"));
			HttpUrl url = HttpUrl.parse("https://pcmap-api.place.naver.com/graphql")
				.newBuilder().build();
			Request request = new Request.Builder()
				.url(url)
				.post(reqBody)
				// GraphQL 요청에도 브라우저와 동일한 User-Agent 사용
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
				// Referer도 브라우저에서 사용하는 형식으로 설정
				.header("Referer", "https://pcmap.place.naver.com/restaurant/" + placeId + "/home?from=map&locale=ko")
				.header("Content-Type", "application/json")
				.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					log.warn("callGraphqlForDetail 실패: {}", response);
					return objectMapper.createArrayNode();
				}
				String bodyStr = response.body().string();
				return objectMapper.readTree(bodyStr);
			}
		} catch (Exception e) {
			log.error("callGraphqlForDetail error", e);
			return objectMapper.createArrayNode();
		}
	}

	// ====== 메뉴 문자열 파싱 ======
	private List<RestaurantCrawlingMenuDto> parseMenus(String menuStr) {
		List<RestaurantCrawlingMenuDto> menus = new ArrayList<>();
		if (menuStr == null || menuStr.isEmpty()) {
			return menus;
		}
		String[] parts = menuStr.split("\\|");
		for (String part : parts) {
			part = part.trim();
			if (!part.isEmpty()) {
				int lastSpace = part.lastIndexOf(" ");
				RestaurantCrawlingMenuDto menu = new RestaurantCrawlingMenuDto();
				if (lastSpace != -1) {
					String name = part.substring(0, lastSpace).trim();
					String price = part.substring(lastSpace + 1).trim();
					menu.setMenuName(name);
					menu.setMenuPrice(price);
				} else {
					menu.setMenuName(part);
					menu.setMenuPrice("");
				}
				menus.add(menu);
			}
		}
		return menus;
	}
}

package com.example.ddo_pay.restaurant.service.crawling;

import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.response.ResponsePositionDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingMenuDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingStoreDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NaverCrawlingServiceImpl implements NaverCrawlingService {


	private final OkHttpClient httpClient = new OkHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public List<RestaurantCrawlingStoreDto> getCrawlingInfo(RestaurantCrawlingRequestDto requestDto) {
		// 1) placeName + addressName
		String combinedQuery = requestDto.getPlaceName() + " " + requestDto.getAddressName();

		// 2) (Optional) allSearch: placeId, lat, lng, resName, etc.
		Map<String, Object> allSearchData = callAllSearch(combinedQuery);
		String placeId = (String) allSearchData.get("placeId");
		Double latFromAllSearch = (Double) allSearchData.get("lat");
		Double lngFromAllSearch = (Double) allSearchData.get("lng");

		log.info("[allSearch] placeId={}, lat={}, lng={}", placeId, latFromAllSearch, lngFromAllSearch);

		// 3) Selenium 크롤링
		WebDriver driver = null;
		List<RestaurantCrawlingStoreDto> results = new ArrayList<>();
		try {
			driver = createWebDriver();  // Selenium Driver
			driver.get("https://map.naver.com/");

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement searchBox = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.input_search"))
			);

			searchBox.clear();
			searchBox.sendKeys(combinedQuery);
			searchBox.sendKeys(Keys.ENTER);

			// iframe 전환
			Thread.sleep(2000);
			driver.switchTo().frame("searchIframe");

			List<WebElement> shopLinks = wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.cssSelector("div>.place_bluelink>.TYaxT, div.place_bluelink>span.YwYLL")
				)
			);

			// 최대 1개만 (또는 n개)
			int maxCount = Math.min(1, shopLinks.size());
			for (int i = 0; i < maxCount; i++) {
				WebElement shop = shopLinks.get(i);
				clickElementWithWait(driver, shop, 5);

				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				shortWait.until(d -> d.getCurrentUrl().contains("/place/"));

				driver.switchTo().defaultContent();
				new WebDriverWait(driver, Duration.ofSeconds(5))
					.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe#entryIframe")));

				RestaurantCrawlingStoreDto dto = new RestaurantCrawlingStoreDto();
				extractStoreInfo(driver, dto);
				extractMenuInfo(driver, dto);

				dto.setPlaceId(placeId);

				// allSearch에서 lat/lng를 가져왔으면 여기에 세팅할 수도 있음
				if (latFromAllSearch != null && lngFromAllSearch != null) {
					ResponsePositionDto pos = new ResponsePositionDto();
					pos.setLat(latFromAllSearch);
					pos.setLng(lngFromAllSearch);
					dto.setPosition(pos);
				}

				results.add(dto);

				// 다음 가게 준비
				driver.switchTo().defaultContent();
				driver.switchTo().frame("searchIframe");
			}

		} catch (Exception e) {
			log.error("[ERROR] 크롤링 중 예외 발생", e);
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}

		// 4) (Optional) GraphQL detail
		if (placeId != null) {
			JsonNode gqlNode = callGraphqlForDetail(placeId);
			// parse announcements or other info
			log.info("GraphQL node: {}", gqlNode.toPrettyString());
			// 필요시 results.get(0)에 추가
		}

		return results;
	}

	// ====== 비공식 allSearch API ======
	private Map<String, Object> callAllSearch(String query) {
		Map<String, Object> result = new HashMap<>();
		result.put("placeId", null);
		result.put("lat", null);
		result.put("lng", null);

		try {
			HttpUrl url = HttpUrl.parse("https://map.naver.com/p/api/search/allSearch")
				.newBuilder()
				.addQueryParameter("query", query)
				.addQueryParameter("type", "all")
				.addQueryParameter("searchCoord", "129.0680439000028;35.154616899999")
				.addQueryParameter("boundary", "")
				.build();

			Request request = new Request.Builder()
				.url(url)
				.header("User-Agent", "Mozilla/5.0 ...") // 필요시
				.header("Referer", "https://map.naver.com")
				.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					log.warn("allSearch fail: {}", response);
					return result;
				}
				String bodyStr = response.body().string();
				JsonNode root = objectMapper.readTree(bodyStr);
				JsonNode boundary = root.path("result").path("place").path("boundary");
				if (boundary.isArray() && boundary.size() >= 2) {
					double lng = boundary.get(0).asDouble();
					double lat = boundary.get(1).asDouble();
					result.put("lat", lat);
					result.put("lng", lng);
				}

				JsonNode listArr = root.path("result").path("place").path("list");
				if (listArr.isArray() && listArr.size() > 0) {
					JsonNode first = listArr.get(0);
					String placeId = first.path("id").asText(null);
					result.put("placeId", placeId);
					// resName, address, etc. 필요하면 추가
				}
			}
		} catch (Exception e) {
			log.error("callAllSearch error: ", e);
		}

		return result;
	}

	// ====== 비공식 GraphQL API ======
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

			RequestBody reqBody = RequestBody.create(
				gqlBody, MediaType.parse("application/json")
			);

			HttpUrl url = HttpUrl.parse("https://pcmap-api.place.naver.com/graphql")
				.newBuilder().build();

			Request request = new Request.Builder()
				.url(url)
				.post(reqBody)
				.header("User-Agent", "Mozilla/5.0 ...")
				.header("Referer", "https://pcmap.place.naver.com/restaurant/" + placeId + "/home?from=map&locale=ko")
				.header("Content-Type", "application/json")
				.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					log.warn("callGraphqlForDetail fail: {}", response);
					return objectMapper.createArrayNode(); // empty array
				}
				String bodyStr = response.body().string();
				JsonNode root = objectMapper.readTree(bodyStr);
				return root;
			}
		} catch (Exception e) {
			log.error("callGraphqlForDetail error", e);
			return objectMapper.createArrayNode();
		}
	}


	private WebDriver createWebDriver() {
		WebDriverManager.chromedriver().setup();
		// 필요 시 Headless, 이미지 차단 옵션 설정
		// ChromeOptions options = new ChromeOptions();
		// options.addArguments("--headless=new");
		// return new ChromeDriver(options);
		return new ChromeDriver();
	}

	private void clickElementWithWait(WebDriver driver, WebElement element, int timeoutSeconds) {
		new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
				.until(ExpectedConditions.elementToBeClickable(element));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	/**
	 * 매장 정보 추출
	 * - 가게명, 주소, 대표 이미지, 별점, 위도/경도 등
	 */
	private void extractStoreInfo(WebDriver driver, RestaurantCrawlingStoreDto dto) {
		try {
			WebElement nameEl = driver.findElement(By.cssSelector("span.GHAhO"));
			dto.setPlaceName(nameEl.getText().trim());
		} catch (NoSuchElementException e) {
			dto.setPlaceName("");
		}

		try {
			WebElement imgEl = driver.findElement(By.cssSelector(".fNygA>a>img"));
			String mainImg = imgEl.getAttribute("src");
			dto.setMainImageUrl(mainImg != null ? mainImg : "");
		} catch (NoSuchElementException e) {
			dto.setMainImageUrl("");
		}

		try {
			WebElement addressEl = driver.findElement(By.cssSelector("span.LDgIH"));
			dto.setAddressName(addressEl.getText().trim());
		} catch (NoSuchElementException e) {
			dto.setAddressName("");
		}

		try {
			// 별점 예: "별점4.81" 텍스트
			WebElement ratingEl = driver.findElement(By.cssSelector("div.dAsGb > span.PXMot.LXIwF"));
			String ratingText = ratingEl.getText().trim(); // "별점4.81"
			ratingText = ratingText.replace("별점", "").trim(); // "4.81"
			BigDecimal starValue = new BigDecimal(ratingText);
			dto.setStarRating(starValue);
		} catch (NoSuchElementException e) {
			dto.setStarRating(BigDecimal.ZERO);
		}

		// URL에서 lat,lng 추출(옵션)
		try {
			String currentUrl = driver.getCurrentUrl();
			// 예) https://map.naver.com/v5/entry/place/35418577?c=128.854346,35.0933107,17,0,0,0,dh
			if (currentUrl.contains("?c=")) {
				String[] parts = currentUrl.split("\\?c=");
				if (parts.length > 1) {
					String cParam = parts[1].split("&")[0];  // "128.854346,35.0933107,17,0,0,0,dh"
					String[] coords = cParam.split(",");
					if (coords.length >= 2) {
						Double lngVal = Double.parseDouble(coords[0]);
						Double latVal = Double.parseDouble(coords[1]);
						ResponsePositionDto pos = new ResponsePositionDto();
						pos.setLng(lngVal);
						pos.setLat(latVal);

						dto.setPosition(pos); // dto.position = pos
					}
				}
			}
		} catch (Exception e) {
			dto.setPosition(null);
		}

		try {
			WebElement infoEl = driver.findElement(By.cssSelector("div.T8RFa"));
			dto.setStoreInfo(infoEl.getText().trim());
		} catch (NoSuchElementException e) {
			dto.setStoreInfo("");
		}
	}


	/**
	 * 메뉴 목록 추출
	 */
	private void extractMenuInfo(WebDriver driver, RestaurantCrawlingStoreDto dto) {
		List<RestaurantCrawlingMenuDto> menuList = new ArrayList<>();

		try {
			// 1) “메뉴” 탭 요소 찾기
			//    (Python과 달리, 자바는 for-loop or stream을 써서 텍스트 비교)
			List<WebElement> menuTabs = driver.findElements(By.cssSelector(".flicking-camera > a"));
			WebElement foundMenuTab = null;

			// find the tab
			for (WebElement tab : menuTabs) {
				if ("메뉴".equals(tab.getText().trim())) {
					foundMenuTab = tab;
					break;
				}
			}

			// if not found, return...
			if (foundMenuTab == null) {
				log.warn("메뉴 탭이 존재하지 않습니다.");
				dto.setMenus(menuList);
				return;
			}

			// 이제 foundMenuTab는 변경되지 않음 → effectively final
			WebElement finalMenuTab = foundMenuTab;

			clickElementWithWait(driver, finalMenuTab, 3);

			new WebDriverWait(driver, Duration.ofSeconds(3)).until(d ->
				"true".equals(finalMenuTab.getAttribute("aria-selected"))
			);

			// 4) 메뉴 목록이 표시될 때까지 대기
			new WebDriverWait(driver, Duration.ofSeconds(5))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.E2jtL")));

			// 4) 이제 li.E2jtL 파싱
			List<WebElement> menuEls = driver.findElements(By.cssSelector("li.E2jtL"));
			for (WebElement menuEl : menuEls) {
				RestaurantCrawlingMenuDto menuDto = new RestaurantCrawlingMenuDto();

				// 메뉴 이름
				try {
					WebElement nameEl2 = menuEl.findElement(By.cssSelector("span.lPzHi"));
					menuDto.setMenuName(nameEl2.getText().trim());
				} catch (NoSuchElementException e) {
					menuDto.setMenuName("");
				}

				// 메뉴 소개
				try {
					WebElement descEl = menuEl.findElement(By.cssSelector("div.kPogF"));
					menuDto.setMenuDesc(descEl.getText().trim());
				} catch (NoSuchElementException e) {
					menuDto.setMenuDesc("");
				}

				// 메뉴 가격
				try {
					WebElement priceEl = menuEl.findElement(By.cssSelector("div.GXS1X"));
					String priceStr = priceEl.getAttribute("textContent");
					menuDto.setMenuPrice(priceStr != null ? priceStr.trim() : "");
				} catch (NoSuchElementException e) {
					menuDto.setMenuPrice("");
				}

				// 메뉴 이미지
				try {
					WebElement imgEl = menuEl.findElement(By.cssSelector("div.place_thumb img"));
					String menuImg = imgEl.getAttribute("src");
					menuDto.setMenuImage(menuImg != null ? menuImg : "");
				} catch (NoSuchElementException e) {
					menuDto.setMenuImage("");
				}

				menuList.add(menuDto);
			}

		} catch (Exception e) {
			log.warn("메뉴 파싱 중 예외 or 메뉴 탭 없음: {}", e.getMessage());
		}

		dto.setMenus(menuList);
	}

}

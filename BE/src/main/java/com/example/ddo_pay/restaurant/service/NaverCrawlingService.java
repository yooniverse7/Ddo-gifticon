package com.example.ddo_pay.restaurant.service;

import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingMenuDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingStoreDto;
import com.example.ddo_pay.restaurant.dto.response.ResponsePositionDto;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NaverCrawlingService {

	/**
	 * 1) 새로 추가한 메서드
	 *    - 컨트롤러에서 "requestDto"를 넘겨주면,
	 *      내부적으로 "addressName"을 꺼내서 crawlStoreDetailsByAddress(...)를 재활용합니다.
	 */
	public List<RestaurantCrawlingStoreDto> getCrawlingInfo(RestaurantCrawlingRequestDto requestDto) {
		// DTO 내부에 "addressName" 필드가 있다고 가정
		String address = requestDto.getAddressName();

		// 필요하다면 placeName도 쓸 수 있음
		// String placeName = requestDto.getPlaceName();

		// 여기서 "crawlStoreDetailsByAddress" 재사용
		return crawlStoreDetailsByAddress(address);
	}

	public List<RestaurantCrawlingStoreDto> crawlStoreDetailsByAddress(String address) {
		List<RestaurantCrawlingStoreDto> resultList = new ArrayList<>();

		// 1) 브라우저 드라이버 설정
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			// 2) 네이버 지도 열기
			driver.get("https://map.naver.com/");

			// 3) 검색창 대기 + 주소 입력
			WebElement searchBox = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.input_search"))
			);
			searchBox.sendKeys(address);
			searchBox.sendKeys(Keys.ENTER);

			// 4) 검색 결과 로딩 대기 후, 검색 결과 iframe으로 전환
			Thread.sleep(2000); // 간단 대기(추가로 WebDriverWait 사용 가능)
			JavascriptExecutor js = (JavascriptExecutor) driver;
			while (!(Boolean) js.executeScript("return document.querySelector('iframe#searchIframe') !== null;")) {
				Thread.sleep(500);
			}
			driver.switchTo().frame("searchIframe");

			// 5) 검색 결과에서 매장 링크들 가져오기
			List<WebElement> shopLinks = wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.cssSelector("div>.place_bluelink>.TYaxT, div.place_bluelink>span.YwYLL")
				)
			);

			// 최대 3개 매장만 수집
			for (int i = 0; i < Math.min(3, shopLinks.size()); i++) {
				WebElement shop = shopLinks.get(i);

				// (A) 매장 클릭 (자바스크립트로 강제 클릭)
				js.executeScript("arguments[0].click();", shop);

				// 상세 페이지 이동 대기
				while (!driver.getCurrentUrl().contains("/place/")) {
					js.executeScript("arguments[0].click();", shop);
					Thread.sleep(1000);
				}

				// (B) 상세 페이지 iframe: entryIframe 전환
				while (true) {
					try {
						driver.switchTo().defaultContent();
						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe#entryIframe")));
						break;
					} catch (NoSuchFrameException e) {
						Thread.sleep(500);
					}
				}

				// ★ 새로운 DTO 생성
				RestaurantCrawlingStoreDto dto = new RestaurantCrawlingStoreDto();

				// (예시) ID, 경도/위도 등을 가져오려면 별도 로직 추가
				// dto.setId(123L); // 실제 ID 추출 로직이 있다면 여기서 셋팅
				// ResponsePositionDto position = new ResponsePositionDto();
				// position.setLat(37.123456); // 실제 위도
				// position.setLng(127.123456); // 실제 경도
				// dto.setPosition(position);

				// 1) 매장 이름
				WebElement nameEl = driver.findElement(By.cssSelector("span.GHAhO"));
				dto.setPlaceName(nameEl.getText());

				// 2) 이미지 URL
				String mainImg = driver.findElement(By.cssSelector(".fNygA>a>img"))
					.getDomAttribute("src");
				dto.setMainImageUrl(mainImg);

				// 3) 매장 주소
				WebElement addressEl = driver.findElement(By.cssSelector("span.LDgIH"));
				dto.setAddressName(addressEl.getText());

				// (C) "정보" 탭 클릭해서 매장 소개 가져오기
				List<WebElement> menuItems = driver.findElements(By.cssSelector(".flicking-camera>a"));
				WebElement infoTab = null;
				for (WebElement menuItem : menuItems) {
					String menuText = menuItem.getText().trim();
					if (menuText.equals("정보")) {
						infoTab = menuItem;
						break;
					}
				}

				if (infoTab != null) {
					js.executeScript("arguments[0].click();", infoTab);
					while (!"true".equals(infoTab.getAttribute("aria-selected"))) {
						js.executeScript("arguments[0].click();", infoTab);
						Thread.sleep(500);
					}
					try {
						WebElement infoEl = driver.findElement(By.cssSelector("div.T8RFa"));
						dto.setStoreInfo(infoEl.getText());
					} catch (NoSuchElementException e) {
						dto.setStoreInfo("(매장 소개 없음)");
					}
				} else {
					dto.setStoreInfo("(정보 탭이 없습니다)");
				}

				// (E) "메뉴" 탭 클릭
				WebElement menuTab = null;
				for (WebElement t : menuItems) {
					String tabText = t.getText().trim();
					if (tabText.equals("메뉴")) {
						menuTab = t;
						break;
					}
				}

				if (menuTab != null) {
					js.executeScript("arguments[0].click();", menuTab);
					while (!"true".equals(menuTab.getAttribute("aria-selected"))) {
						js.executeScript("arguments[0].click();", menuTab);
						Thread.sleep(500);
					}

					// (F) 메뉴 목록 파싱
					try {
						List<WebElement> menuElements = driver.findElements(By.cssSelector("div.yQlqY"));
						List<RestaurantCrawlingMenuDto> menuList = new ArrayList<>();

						for (WebElement menuEl : menuElements) {
							RestaurantCrawlingMenuDto menuDto = new RestaurantCrawlingMenuDto();

							// 메뉴 이름
							try {
								WebElement nameEl2 = menuEl.findElement(By.cssSelector("span.lPzHi"));
								menuDto.setMenuName(nameEl2.getText());
							} catch (NoSuchElementException e) {
								menuDto.setMenuName("(메뉴명 없음)");
							}

							// 메뉴 소개
							try {
								WebElement descEl = menuEl.findElement(By.cssSelector(".kPogF"));
								menuDto.setMenuDesc(descEl.getText());
							} catch (NoSuchElementException e) {
								menuDto.setMenuDesc("(소개 없음)");
							}

							// 메뉴 가격
							try {
								WebElement priceEl = menuEl.findElement(By.cssSelector("div.GXS1X"));
								String priceStr = priceEl.getDomAttribute("textContent");
								if (priceStr == null) {
									priceStr = "(가격정보 없음)";
								}
								menuDto.setMenuPrice(priceStr);
							} catch (NoSuchElementException e) {
								menuDto.setMenuPrice("(가격 없음)");
							}

							// 메뉴 이미지
							try {
								String menuImg = menuEl.findElement(By.cssSelector("div.place_thumb img"))
									.getDomAttribute("src");
								menuDto.setMenuImage(menuImg);
							} catch (NoSuchElementException e) {
								menuDto.setMenuImage("(이미지 없음)");
							}

							menuList.add(menuDto);
						}
						dto.setMenus(menuList);

					} catch (NoSuchElementException e) {
						log.warn("메뉴 탭은 있으나, 메뉴 아이템이 없거나 구조가 다릅니다.");
					}

				} else {
					log.info("메뉴 탭이 존재하지 않습니다.");
				}

				// 결과 리스트에 DTO 추가
				resultList.add(dto);

				// (D) 다시 검색 결과 페이지로 돌아가기
				driver.switchTo().defaultContent();
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe#searchIframe")));
			}

		} catch (Exception e) {
			log.error("크롤링 중 에러 발생: {}", e.getMessage());
		} finally {
			// 브라우저 종료
			driver.quit();
		}

		return resultList;
	}
}

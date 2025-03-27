package com.example.ddo_pay.restaurant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ddo_pay.common.util.SecurityUtil;
import com.example.ddo_pay.restaurant.dto.request.CustomMenuRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCreateRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantDeleteRequestDto;
import com.example.ddo_pay.restaurant.dto.response.MenuResponseDto;
import com.example.ddo_pay.restaurant.dto.response.ResponsePositionDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingResponseDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingStoreDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantDetailResponseDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantListItemResponseDto;
import com.example.ddo_pay.restaurant.entity.CustomMenu;
import com.example.ddo_pay.restaurant.entity.Menu;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import com.example.ddo_pay.restaurant.entity.UserRestaurant;
import com.example.ddo_pay.restaurant.repository.CustomMenuRepository;
import com.example.ddo_pay.restaurant.repository.MenuRepository;
import com.example.ddo_pay.restaurant.repository.RestaurantRepository;
import com.example.ddo_pay.restaurant.repository.UserRestaurantRepository;
import com.example.ddo_pay.restaurant.service.RestaurantService;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final UserRestaurantRepository userRestaurantRepository;
	private final MenuRepository menuRepository;
	private final CustomMenuRepository customMenuRepository;
	private final UserRepo userRepo; // 예: 사용자 식별을 위한 repository

	/**
	 * 맛집 등록 로직
	 */
	@Override
	@Transactional
	public void createRestaurant(RestaurantCreateRequestDto requestDto) {

		// 1) 사용자 조회
		User user = userRepo.findById((long) requestDto.getUserId())
				.orElseThrow(() -> new RuntimeException("해당 user가 존재하지 않습니다."));

		// 2) Restaurant 엔티티 빌더로 생성
		//    resName → placeName
		//    resAddress → addressName
		//    resLat/resLng → lat/lng
		//    resImage → mainImageUrl
		Restaurant restaurant = Restaurant.builder()
				.placeName(requestDto.getPlaceName())      // 변경
				.addressName(requestDto.getAddressName())  // 변경
				.lat(requestDto.getPosition().getLat())    // 변경
				.lng(requestDto.getPosition().getLng())    // 변경
				.mainImageUrl(requestDto.getMainImageUrl()) // 변경
				.userIntro(requestDto.getUserIntro())      // 기존 그대로 사용
				.starRating(requestDto.getStarRating())    // 기존 그대로 사용
				.build();

		Restaurant savedRestaurant = restaurantRepository.save(restaurant);

		// 3) UserRestaurant 빌더
		UserRestaurant userRestaurant = UserRestaurant.builder()
				.user(user)
				.restaurant(savedRestaurant)
				.visitedCount(requestDto.getVisitedCount()) // 기본값 0
				.build();

		UserRestaurant savedUserRestaurant = userRestaurantRepository.save(userRestaurant);

		// 4) Menu 목록 등록
		if (requestDto.getMenu() != null && !requestDto.getMenu().isEmpty()) {
			requestDto.getMenu().forEach(menuDto -> {
				Menu menu = Menu.builder()
						.menuName(menuDto.getMenuName())
						.menuPrice(menuDto.getMenuPrice())
						.menuImage(menuDto.getMenuImage())
						.restaurant(savedRestaurant)
						.build();
				menuRepository.save(menu);
			});
		}

		// 5) CustomMenu 목록 등록
		if (requestDto.getCustomMenu() != null && !requestDto.getCustomMenu().isEmpty()) {
			requestDto.getCustomMenu().forEach(customDto -> {
				CustomMenu customMenu = CustomMenu.builder()
						.customMenuName(customDto.getCustomMenuName())
						.customMenuPrice(customDto.getCustomMenuPrice())
						.customMenuImage(customDto.getCustomMenuImage())
						.userRestaurant(savedUserRestaurant)
						.build();
				customMenuRepository.save(customMenu);
			});
		}

		log.info("맛집 등록 완료. restaurantId={}, userId={}",
				savedRestaurant.getId(), user.getId());
	}

	/**
	 * 맛집 해제(삭제) 로직
	 */
	@Override
	@Transactional
	public void removeRestaurant(RestaurantDeleteRequestDto requestDto) {

		// 1) 사용자 조회
		User user = userRepo.findById((long) requestDto.getUserId())
				.orElseThrow(() -> new RuntimeException("해당 user가 존재하지 않습니다."));

		// 2) userId + restaurantId 로 UserRestaurant 조회
		UserRestaurant userRestaurant = userRestaurantRepository
				.findByUser_IdAndRestaurant_Id(user.getId(), requestDto.getRestaurantId())
				.orElseThrow(() -> new RuntimeException("등록된 맛집 정보가 없습니다."));

		// 3) 관계 해제 + DB에서 삭제
		userRestaurantRepository.delete(userRestaurant);

		log.info("맛집 해제 완료. userId={}, restaurantId={}",
				user.getId(),
				requestDto.getRestaurantId());
	}

	/**
	 * 등록된 맛집 리스트 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RestaurantListItemResponseDto> getRegisteredRestaurantList() {
		// 1) 현재 로그인 사용자 식별
		Long userId = SecurityUtil.getUserId();  // 가정

		// 2) userRestaurant 테이블에서 userId로 등록된 목록 조회
		List<UserRestaurant> userResList = userRestaurantRepository.findByUser_Id(userId);

		// 3) 각 UserRestaurant → Restaurant 정보 추출 + DTO 매핑
		List<RestaurantListItemResponseDto> result = new ArrayList<>();
		for (UserRestaurant ur : userResList) {
			Restaurant r = ur.getRestaurant();

			RestaurantListItemResponseDto dto = new RestaurantListItemResponseDto();
			dto.setId(r.getId());

			// resName → placeName
			dto.setPlaceName(r.getPlaceName());

			// resAddress → addressName
			dto.setAddressName(r.getAddressName());

			// resImage → mainImageUrl
			dto.setMainImageUrl(r.getMainImageUrl());

			// visitedCount
			dto.setVisitedCount(ur.getVisitedCount());

			// position
			ResponsePositionDto pos = new ResponsePositionDto();
			pos.setLat(r.getLat());  // resLat → lat
			pos.setLng(r.getLng());  // resLng → lng
			dto.setPosition(pos);

			result.add(dto);
		}

		return result;
	}

	/**
	 * 맛집 상세 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public RestaurantDetailResponseDto getRestaurantDetail(Long restaurantId) {
		// 1) Restaurant 엔티티 조회
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
				.orElseThrow(() -> new RuntimeException("해당 맛집이 존재하지 않습니다."));

		// 2) DTO 변환
		RestaurantDetailResponseDto detailDto = new RestaurantDetailResponseDto();
		detailDto.setRestaurantId(restaurant.getId());

		// resName → placeName
		detailDto.setPlaceName(restaurant.getPlaceName());

		// resAddress → addressName
		detailDto.setAddressName(restaurant.getAddressName());

		// resImage → mainImageUrl
		detailDto.setMainImageUrl(restaurant.getMainImageUrl());

		// 별점 (BigDecimal → double 변환)
		detailDto.setStarRating(
				restaurant.getStarRating() != null
						? restaurant.getStarRating().doubleValue()
						: 0.0
		);

		// userIntro
		detailDto.setUserIntro(restaurant.getUserIntro());

		// 위치 (위도, 경도)
		ResponsePositionDto pos = new ResponsePositionDto();
		pos.setLat(restaurant.getLat());
		pos.setLng(restaurant.getLng());
		detailDto.setPosition(pos);

		// 메뉴 목록
		List<MenuResponseDto> menuDtos = new ArrayList<>();
		for (Menu m : restaurant.getMenuList()) {
			MenuResponseDto mDto = new MenuResponseDto();
			mDto.setMenuName(m.getMenuName());
			mDto.setMenuPrice(String.valueOf(m.getMenuPrice()));
			mDto.setMenuImage(m.getMenuImage());
			menuDtos.add(mDto);
		}
		detailDto.setMenu(menuDtos);

		// 커스텀 메뉴는 userRestaurant 통해 가져올 수도 있음(유저별로 다를 수 있으므로)
		// 여기서는 생략

		return detailDto;
	}

	/**
	 * 커스텀 메뉴 등록
	 */
	@Override
	@Transactional
	public void createCustomMenu(CustomMenuRequestDto requestDto) {
		// 예) userId + restaurantId 로 UserRestaurant 조회
		UserRestaurant userRestaurant = userRestaurantRepository
				.findByUser_IdAndRestaurant_Id(requestDto.getUserId(), requestDto.getRestaurantId())
				.orElseThrow(() -> new RuntimeException("등록되지 않은 맛집입니다."));

		CustomMenu customMenu = CustomMenu.builder()
				.customMenuName(requestDto.getCustomMenuName())
				.customMenuPrice(requestDto.getCustomMenuPrice())
				.customMenuImage(requestDto.getCustomMenuImage())
				.userRestaurant(userRestaurant)
				.build();

		customMenuRepository.save(customMenu);
		log.info("커스텀 메뉴 등록 완료. userId={}, restaurantId={}", requestDto.getUserId(), requestDto.getRestaurantId());
	}

	/**
	 * 커스텀 메뉴 삭제
	 */
	@Override
	@Transactional
	public void deleteCustomMenu(Long customId) {
		if (!customMenuRepository.existsById(customId)) {
			throw new RuntimeException("해당 커스텀 메뉴가 존재하지 않습니다.");
		}
		customMenuRepository.deleteById(customId);
		log.info("커스텀 메뉴 삭제 완료. customId={}", customId);
	}

	/**
	 * 크롤링 정보 조회 (예시)
	 */
	@Override
	@Transactional(readOnly = true)
	public RestaurantCrawlingResponseDto getCrawlingInfo(List<RestaurantCrawlingRequestDto> requestList) {

		RestaurantCrawlingResponseDto response = new RestaurantCrawlingResponseDto();
		List<RestaurantCrawlingStoreDto> stores = new ArrayList<>();

		for (RestaurantCrawlingRequestDto req : requestList) {
			// 가령, 외부 API(NaverCrawlingService 등) 호출해서 데이터 매핑
			RestaurantCrawlingStoreDto store = new RestaurantCrawlingStoreDto();

			// placeName, addressName은 req 객체의 필드를 바로 사용
			store.setPlaceName(req.getPlaceName());
			store.setAddressName(req.getAddressName());

			// position에는 lat/lng만
			ResponsePositionDto pos = new ResponsePositionDto();
			pos.setLat(req.getPosition().getLat());
			pos.setLng(req.getPosition().getLng());
			store.setPosition(pos);

			stores.add(store);
		}

		response.setStores(stores);
		return response;
	}

}

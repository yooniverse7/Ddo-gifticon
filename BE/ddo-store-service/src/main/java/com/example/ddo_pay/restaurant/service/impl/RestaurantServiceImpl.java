package com.example.ddo_pay.restaurant.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.ddo_pay.restaurant.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ddo_pay.common.exception.CustomException;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.common.util.SecurityUtil;
import com.example.ddo_pay.restaurant.dto.request.CustomMenuRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCreateRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantDeleteRequestDto;
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

		if (requestDto.getUserId() == null) {
			throw new CustomException(
				ResponseCode.NO_EXIST_USER,
				"userId",
				"user_id가 누락되었습니다."
			);
		}

		// 1) 사용자 조회
		User user = userRepo.findById((long) requestDto.getUserId())
			.orElseThrow(() -> new CustomException(
				ResponseCode.NO_EXIST_USER,
				"userId",
				"해당 user가 존재하지 않습니다."
			));

		// 2) (placeName, addressName)로 Restaurant 조회
		Optional<Restaurant> existingRestaurantOpt = restaurantRepository.findByPlaceNameAndAddressName(
			requestDto.getPlaceName(),
			requestDto.getAddressName()
		);

		Restaurant restaurant;
		if (existingRestaurantOpt.isPresent()) {
			// 이미 같은 식당 존재 → 재사용
			restaurant = existingRestaurantOpt.get();
		} else {
			// 새로운 식당 엔티티 생성
			restaurant = Restaurant.builder()
				.placeName(requestDto.getPlaceName())
				.addressName(requestDto.getAddressName())
				.lat(requestDto.getPosition().getLat())
				.lng(requestDto.getPosition().getLng())
				.mainImageUrl(requestDto.getMainImageUrl())
				.userIntro(requestDto.getUserIntro())
				.starRating(requestDto.getStarRating())
				.build();

			restaurantRepository.save(restaurant);
		}

		// 3) UserRestaurant 중복 체크: 같은 user + 같은 restaurant id?
		Optional<UserRestaurant> existingUserRes = userRestaurantRepository
			.findByUser_IdAndRestaurant_Id(user.getId(), restaurant.getId());

		if (existingUserRes.isPresent()) {
			// 이미 이 유저가 해당 식당을 등록한 상태
			throw new CustomException(
				ResponseCode.DATA_ALREADY_EXISTS,
				"userId,restaurantId",
				"이미 등록된 맛집입니다."
			);
		}

		// 4) UserRestaurant 새로 생성
		UserRestaurant userRestaurant = UserRestaurant.builder()
			.user(user)
			.restaurant(restaurant)
			.visitedCount(requestDto.getVisitedCount()) // 기본값 0
			.build();

		userRestaurantRepository.save(userRestaurant);

		// 5) Menu 목록 등록
		//   만약 “Restaurant가 처음 생겼을 때만 Menu를 추가”한다면,
		//   “if (!existingRestaurantOpt.isPresent()) { ... }” 조건으로 분기할 수도 있음.
		if (requestDto.getMenu() != null && !requestDto.getMenu().isEmpty()) {
			requestDto.getMenu().forEach(menuDto -> {
				Menu menu = Menu.builder()
					.menuName(menuDto.getMenuName())
					.menuPrice(menuDto.getMenuPrice())
					.menuImage(menuDto.getMenuImage())
					.restaurant(restaurant) // 재사용 or 새로 만든 Restaurant
					.build();
				menuRepository.save(menu);
			});
		}

		// 6) CustomMenu 목록 등록
		//   마찬가지로 “if userRestaurant가 새로 생겼을 때만” 등 정책에 따라 분기 가능
		if (requestDto.getCustomMenu() != null && !requestDto.getCustomMenu().isEmpty()) {
			requestDto.getCustomMenu().forEach(customDto -> {
				CustomMenu customMenu = CustomMenu.builder()
					.customMenuName(customDto.getCustomMenuName())
					.customMenuPrice(customDto.getCustomMenuPrice())
					.customMenuImage(customDto.getCustomMenuImage())
					.userRestaurant(userRestaurant)
					.build();
				customMenuRepository.save(customMenu);
			});
		}

		log.info("맛집 등록 완료. restaurantId={}, userId={}",
			restaurant.getId(), user.getId());
	}


	/**
	 * 맛집 해제(삭제) 로직
	 */
	@Override
	@Transactional
	public void removeRestaurant(RestaurantDeleteRequestDto requestDto) {

		// 1) 사용자 조회
		User user = userRepo.findById((long) requestDto.getUserId())
			.orElseThrow(() -> new CustomException(
				ResponseCode.NO_EXIST_USER,
				"userId",
				"해당 user가 존재하지 않습니다."
			));

		// 2) userId + restaurantId 로 UserRestaurant 조회
		UserRestaurant userRestaurant = userRestaurantRepository
			.findByUser_IdAndRestaurant_Id(user.getId(), requestDto.getRestaurantId())
			.orElseThrow(() -> new CustomException(
				ResponseCode.NO_EXIST_RESTAURANT,
				"restaurantId",
				"등록된 맛집 정보가 없습니다."
			));

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
			.orElseThrow(() -> new CustomException(
				ResponseCode.NO_EXIST_RESTAURANT,  // 예) 400, "등록된 식당이 아닙니다."
				"restaurantId",
				"해당 맛집이 존재하지 않습니다."
			));

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
				.orElseThrow(() -> new CustomException(
					ResponseCode.NO_EXIST_RESTAURANT,
					"restaurantId",
					"등록되지 않은 맛집입니다."
				));

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
			// 기존: throw new RuntimeException("해당 커스텀 메뉴가 존재하지 않습니다.");
			// 수정 후:
			throw new CustomException(
				ResponseCode.NO_EXIST_CUSTOM_MENU,  // 혹은 다른 적절한 코드
				"customId",
				"해당 커스텀 메뉴가 존재하지 않습니다."
			);
		}

		customMenuRepository.deleteById(customId);
		log.info("커스텀 메뉴 삭제 완료. customId={}", customId);
	}

	@Override
	@Transactional
	public void saveCrawlingStoreData(RestaurantCrawlingStoreDto storeDto, Long userId) {
		// 1) 유저 조회
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

		// 2) Restaurant 빌더로 생성/저장
		Restaurant restaurant = Restaurant.builder()
				.placeName(storeDto.getPlaceName())
				.addressName(storeDto.getAddressName())
				.mainImageUrl(storeDto.getMainImageUrl())
				.lat(storeDto.getPosition() != null ? storeDto.getPosition().getLat() : null)
				.lng(storeDto.getPosition() != null ? storeDto.getPosition().getLng() : null)
				.userIntro(storeDto.getUserIntro())
				.starRating(storeDto.getStarRating())
				.build();

		restaurantRepository.save(restaurant);

		// 3) 메뉴 목록
		if (storeDto.getMenus() != null) {
			for (RestaurantCrawlingMenuDto menuDto : storeDto.getMenus()) {
				Menu menu = Menu.builder()
						.menuName(menuDto.getMenuName())
						// menuPrice, menuImage 등도 필요하면 세팅
						.restaurant(restaurant)
						.build();

				menuRepository.save(menu);
			}
		}

		// 4) userRestaurant 빌더로 생성/저장
		UserRestaurant userRestaurant = UserRestaurant.builder()
				.user(user)
				.restaurant(restaurant)
				.build();

		userRestaurantRepository.save(userRestaurant);

		log.info("크롤링된 매장 저장 완료: {}, userId={}", restaurant.getPlaceName(), userId);
	}


}

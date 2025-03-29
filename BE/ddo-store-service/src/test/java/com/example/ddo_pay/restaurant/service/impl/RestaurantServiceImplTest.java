package com.example.ddo_pay.restaurant.service.impl;

import com.example.ddo_pay.common.exception.CustomException;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCreateRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantMenuRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RequestPositionDto;
import com.example.ddo_pay.restaurant.entity.CustomMenu;
import com.example.ddo_pay.restaurant.entity.Menu;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import com.example.ddo_pay.restaurant.entity.UserRestaurant;
import com.example.ddo_pay.restaurant.repository.CustomMenuRepository;
import com.example.ddo_pay.restaurant.repository.MenuRepository;
import com.example.ddo_pay.restaurant.repository.RestaurantRepository;
import com.example.ddo_pay.restaurant.repository.UserRestaurantRepository;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private UserRestaurantRepository userRestaurantRepository;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private CustomMenuRepository customMenuRepository;
	@Mock
	private UserRepo userRepo;

	@InjectMocks
	private RestaurantServiceImpl restaurantService; // 테스트 대상 Service

	private RestaurantCreateRequestDto requestDto;

	@BeforeEach
	void setUp() {
		// 기본 Request DTO 세팅
		// user_id=1, placeName="김밥천국", addressName="서울 종로구 어딘가"
		requestDto = new RestaurantCreateRequestDto();
		requestDto.setUserId(1L);
		requestDto.setPlaceName("김밥천국");
		requestDto.setAddressName("서울 종로구 어딘가");
		requestDto.setPosition(new RequestPositionDto(37.1234, 127.5678));
		requestDto.setStarRating(BigDecimal.valueOf(4.5));
		requestDto.setVisitedCount(0);

		// 메뉴 예시
		RestaurantMenuRequestDto menuReq = new RestaurantMenuRequestDto();
		menuReq.setUserId(1L);
		menuReq.setMenuName("라면");
		menuReq.setMenuPrice(4000);
		menuReq.setMenuImage("http://example.com/images/ramen.jpg");
		requestDto.setMenu(List.of(menuReq));

		// CustomMenu 등 추가 시 필요
	}

	@Test
	@DisplayName("createRestaurant - 존재하지 않는 User면 예외(CustomException.NO_EXIST_USER)")
	void createRestaurant_noExistUser() {
		// given
		// userRepo.findById(1L) => Optional.empty()
		when(userRepo.findById(1L)).thenReturn(Optional.empty());

		// when
		CustomException ex = assertThrows(CustomException.class,
			() -> restaurantService.createRestaurant(requestDto)
		);

		// then
		// (A) 기본적인 ResponseCode 검사
		assertEquals(ResponseCode.NO_EXIST_USER, ex.getResponseCode());

		// (B) ErrorResponse를 생성해 필드와 메시지를 검증
		var errorResponse = ex.toErrorResponse();
		assertEquals("userId", errorResponse.getField());
		assertEquals("해당 user가 존재하지 않습니다.", errorResponse.getMessage());

		// verify userRepo
		verify(userRepo, times(1)).findById(1L);
		verifyNoMoreInteractions(restaurantRepository, userRestaurantRepository, menuRepository, customMenuRepository);
	}

	@Test
	@DisplayName("createRestaurant - placeName+addressName 이미 존재 & 동일 userRestaurant 중복 시 예외(DATA_ALREADY_EXISTS)")
	void createRestaurant_sameUserRestaurantExists() {
		// given
		// user 존재
		User mockUser = User.builder().id(1L).name("TestUser").build();
		when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

		// restaurantRepository: placeName+addressName 이미 존재
		Restaurant mockRestaurant = Restaurant.builder()
			.id(100L)
			.placeName("김밥천국")
			.addressName("서울 종로구 어딘가")
			.build();
		// findByPlaceNameAndAddressName => Optional.of(mockRestaurant)
		when(restaurantRepository.findByPlaceNameAndAddressName("김밥천국", "서울 종로구 어딘가"))
			.thenReturn(Optional.of(mockRestaurant));

		// userRestaurantRepository: user_id=1 & restaurant_id=100 => 이미 존재
		UserRestaurant mockUserRes = UserRestaurant.builder()
			.id(999L)
			.user(mockUser)
			.restaurant(mockRestaurant)
			.build();
		when(userRestaurantRepository.findByUser_IdAndRestaurant_Id(1L, 100L))
			.thenReturn(Optional.of(mockUserRes));

		// when
		CustomException ex = assertThrows(CustomException.class,
			() -> restaurantService.createRestaurant(requestDto)
		);

		// then
		// (1) ResponseCode 검사
		assertEquals(ResponseCode.DATA_ALREADY_EXISTS, ex.getResponseCode());

		// (2) ErrorResponse를 통해 field/message 확인
		var errorResponse = ex.toErrorResponse();
		assertEquals("userId,restaurantId", errorResponse.getField());
		assertEquals("이미 등록된 맛집입니다.", errorResponse.getMessage());

		// verify calls
		verify(userRepo, times(1)).findById(1L);
		verify(restaurantRepository, times(1))
			.findByPlaceNameAndAddressName("김밥천국","서울 종로구 어딘가");
		verify(userRestaurantRepository, times(1))
			.findByUser_IdAndRestaurant_Id(1L, 100L);
		verifyNoMoreInteractions(userRestaurantRepository, menuRepository, customMenuRepository);
	}

	@Test
	@DisplayName("createRestaurant - placeName+addressName 이미 존재하지만 다른 User이면 새로운 UserRestaurant 생성")
	void createRestaurant_restaurantExistsDifferentUser() {
		// given
		// user 1 존재
		User mockUser = User.builder().id(1L).name("User1").build();
		when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

		// restaurantRepository: "김밥천국","서울 종로구 어딘가" => id=100
		Restaurant mockRestaurant = Restaurant.builder()
			.id(100L)
			.placeName("김밥천국")
			.addressName("서울 종로구 어딘가")
			.build();
		when(restaurantRepository.findByPlaceNameAndAddressName("김밥천국","서울 종로구 어딘가"))
			.thenReturn(Optional.of(mockRestaurant));

		// userRestaurantRepository: findByUser_IdAndRestaurant_Id => empty (즉, user=1, restaurant=100이 연결X)
		when(userRestaurantRepository.findByUser_IdAndRestaurant_Id(1L, 100L))
			.thenReturn(Optional.empty());

		// mock save
		when(userRestaurantRepository.save(any(UserRestaurant.class)))
			.thenAnswer(invocation -> {
				UserRestaurant ur = invocation.getArgument(0, UserRestaurant.class);
				// 강제로 PK=999L 세팅
				org.springframework.test.util.ReflectionTestUtils.setField(ur, "id", 999L);
				return ur;
			});

		// when
		restaurantService.createRestaurant(requestDto);

		// then
		// 검증: userRestaurant가 새로 생성되었는지
		verify(userRepo, times(1)).findById(1L);
		verify(restaurantRepository, times(1))
			.findByPlaceNameAndAddressName("김밥천국","서울 종로구 어딘가");
		verify(userRestaurantRepository, times(1))
			.findByUser_IdAndRestaurant_Id(1L, 100L);
		// restaurantRepository.save(...)는 호출 안 됨(이미 존재) => verify(restaurantRepository, never()).save(any(Restaurant.class));
		verify(userRestaurantRepository, times(1)).save(any(UserRestaurant.class));

		// Menu 등록
		verify(menuRepository, times(1)).save(any(Menu.class)); // 한 개
		// CustomMenu가 없으므로 customMenuRepository.save()는 안 호출
		verifyNoInteractions(customMenuRepository);
	}

	@Test
	@DisplayName("createRestaurant - 식당이 전혀 없을 때 (user 존재, restaurant도 없음) => 새 restaurant & userRestaurant 생성")
	void createRestaurant_newRestaurant() {
		// given
		// user=1 존재
		User mockUser = User.builder().id(1L).name("User1").build();
		when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

		// restaurantRepository => Optional.empty() => 새 식당 생성
		when(restaurantRepository.findByPlaceNameAndAddressName("김밥천국","서울 종로구 어딘가"))
			.thenReturn(Optional.empty());

		// mock saving restaurant
		when(restaurantRepository.save(any(Restaurant.class)))
			.thenAnswer(invocation -> {
				Restaurant r = invocation.getArgument(0, Restaurant.class);
				org.springframework.test.util.ReflectionTestUtils.setField(r, "id", 200L);
				return r;
			});

		// userRestaurantRepository => no existing => new
		when(userRestaurantRepository.findByUser_IdAndRestaurant_Id(1L, 200L))
			.thenReturn(Optional.empty());
		when(userRestaurantRepository.save(any(UserRestaurant.class)))
			.thenAnswer(invocation -> {
				UserRestaurant ur = invocation.getArgument(0, UserRestaurant.class);
				org.springframework.test.util.ReflectionTestUtils.setField(ur, "id", 999L);
				return ur;
			});

		// mock menuRepository for saving
		when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		restaurantService.createRestaurant(requestDto);

		// then
		verify(userRepo, times(1)).findById(1L);
		verify(restaurantRepository, times(1))
			.findByPlaceNameAndAddressName("김밥천국","서울 종로구 어딘가");
		verify(restaurantRepository, times(1)).save(any(Restaurant.class));  // 새 식당
		verify(userRestaurantRepository, times(1))
			.findByUser_IdAndRestaurant_Id(1L, 200L);
		verify(userRestaurantRepository, times(1)).save(any(UserRestaurant.class));
		verify(menuRepository, times(1)).save(any(Menu.class));
		// no custom menu => no customMenuRepository call
		verifyNoInteractions(customMenuRepository);
	}
}

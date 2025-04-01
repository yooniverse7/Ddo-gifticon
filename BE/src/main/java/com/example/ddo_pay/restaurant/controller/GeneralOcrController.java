package com.example.ddo_pay.restaurant.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.ddo_pay.restaurant.entity.Restaurant;
import com.example.ddo_pay.restaurant.entity.UserRestaurant;
import com.example.ddo_pay.restaurant.repository.RestaurantRepository;
import com.example.ddo_pay.restaurant.repository.UserRestaurantRepository;
import com.example.ddo_pay.restaurant.service.receipt.impl.NaverMapSearchService;
import com.example.ddo_pay.restaurant.service.receipt.impl.OcrProcessService;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class GeneralOcrController {

    private final OcrProcessService ocrProcessService;      // OCR
    private final NaverMapSearchService naverMapSearchService; // 네이버 지도 검색
    private final RestaurantRepository restaurantRepository;
    private final UserRestaurantRepository userRestaurantRepository;
    private final UserRepo userRepo; // 유저 조회용
    // (또는 RestaurantServiceImpl를 주입받아 처리 로직 위임)

    /**
     *  POST /api/ocr/scan
     *  - form-data: file (영수증 이미지), userId
     */
    @PostMapping("/scan")
    public ResponseEntity<?> scanReceipt(
        @RequestParam("file") MultipartFile file,
        @RequestParam("userId") Long userId
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        // 1) user 조회
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 유저: " + userId));

        // 2) OCR 통해 placeName, addressName 추출
        var extracted = ocrProcessService.extractPlaceInfo(file);
        String placeName = extracted.getPlaceName();
        String addressName = extracted.getAddressName();

        response.put("placeName", placeName);
        response.put("addressName", addressName);

        // 3) 네이버 지도 검색 -> placeId
        String query = placeName + " " + addressName;
        String placeId = naverMapSearchService.getPlaceIdFromQuery(query);

        if (placeId == null) {
            // 검색 실패
            response.put("message", "네이버 지도 검색 결과가 없습니다.");
            return ResponseEntity.ok(response);
        }
        response.put("placeId", placeId);

        // 4) DB에서 placeId로 Restaurant 조회 (없으면 생성)
        Restaurant restaurant = restaurantRepository.findByPlaceId(placeId)
            .orElseGet(() -> {
                // 새로 생성
                Restaurant r = Restaurant.builder()
                    .placeId(placeId)
                    .placeName(placeName)
                    .addressName(addressName)
                    .lat(null)
                    .lng(null)
                    .build();
                restaurantRepository.save(r);
                return r;
            });

        response.put("restaurantId", restaurant.getId());

        // 5) userRestaurant 조회
        Optional<UserRestaurant> optUr = userRestaurantRepository
            .findByUser_IdAndRestaurant_Id(user.getId(), restaurant.getId());
        if (optUr.isPresent()) {
            // 이미 등록된 맛집
            UserRestaurant ur = optUr.get();
            ur.setVisitedCount(ur.getVisitedCount() + 1);
            // userRestaurantRepository.save(ur); // 영속상태면 생략 가능

            response.put("alreadyRegistered", true);
            response.put("visitedCount", ur.getVisitedCount());
            response.put("message", "이미 등록된 맛집, 방문 횟수 갱신 완료");
        } else {
            // 등록되지 않은 맛집 -> 등록 여부 confirm
            response.put("alreadyRegistered", false);
            response.put("needRegisterConfirm", true);
            response.put("message", "등록되지 않은 맛집, 등록 여부를 확인하세요.");
        }

        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     *  POST /api/ocr/confirm
     *  - 등록 여부 Yes 선택 시 호출: userId, restaurantId
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmRegister(
        @RequestParam("userId") Long userId,
        @RequestParam("restaurantId") Long restaurantId
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 유저: " + userId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 식당: " + restaurantId));

        // 중복 확인
        Optional<UserRestaurant> optUr = userRestaurantRepository
            .findByUser_IdAndRestaurant_Id(userId, restaurantId);
        if (optUr.isPresent()) {
            // 이미 등록됨
            response.put("success", true);
            response.put("message", "이미 등록된 맛집입니다.");
            return ResponseEntity.ok(response);
        }

        // 새 userRestaurant
        UserRestaurant ur = UserRestaurant.builder()
            .user(user)
            .restaurant(restaurant)
            .visitedCount(1)
            .build();
        userRestaurantRepository.save(ur);

        response.put("success", true);
        response.put("message", "맛집 등록 완료");
        response.put("visitedCount", ur.getVisitedCount());
        return ResponseEntity.ok(response);
    }
}
package com.example.ddo_pay.gift.service.impl;

import com.example.ddo_pay.common.config.S3.S3Service;
import com.example.ddo_pay.common.dto.TokenData;
import com.example.ddo_pay.common.exception.CustomException;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.common.util.RedisHandler;
import com.example.ddo_pay.gift.dto.GiftCheckResponseDto;
import com.example.ddo_pay.gift.dto.GiftRefundRequestDto;
import com.example.ddo_pay.gift.dto.GiftSelectResponseDto;
import com.example.ddo_pay.gift.dto.create.GiftCreateRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftCheckRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftDetailResponseDto;
import com.example.ddo_pay.gift.dto.update.GiftUpdateRequestDto;
import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.GiftBox;
import com.example.ddo_pay.gift.entity.USED;
import com.example.ddo_pay.gift.repository.GiftBoxRepository;
import com.example.ddo_pay.gift.repository.GiftRepository;
import com.example.ddo_pay.gift.service.GiftService;
import com.example.ddo_pay.pay.service.PayService;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import com.example.ddo_pay.restaurant.repository.RestaurantRepository;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.service.impl.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {

    private final GiftRepository giftRepository;
    private final GiftBoxRepository giftBoxRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final PayService payService;
    private final S3Service s3Service;

    private static final Logger log = Logger.getLogger(GiftServiceImpl.class.getName());
    private final RedisHandler redisHandler;

    /* 맛집 기반으로 기프티콘을 생성할 수 있다. 현재 생성할 때, 같이 이뤄져야 할 결제 로직 빠져있다. */
    @Override
    public void create(GiftCreateRequestDto dto, Long userId, MultipartFile image) throws IOException {

        // 1. 맛집의 메뉴들의 정보와 사용자 커스텀 정보를 받아서 DB에 저장한다.
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));
        Restaurant restaurant = restaurantRepository.findById((long) dto.getResId()).orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_RESTAURANT));



        log.info("메뉴 조합 : " + dto.getMenuName());

        // S3 이미지 업로드
        String imageUrl = s3Service.uploadFile(image);

        Gift gift = Gift.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .message(dto.getMessage())
                .phoneNum(dto.getPhoneNum())
                .menuCombination(dto.getMenuName())
                .user(user)
                .image(imageUrl)
                .restaurant(restaurant)
                .usedStatus(USED.BEFORE_USE)
                .expirationDate(LocalDateTime.now().plusMonths(3))
                .build();



        // 기프티콘 저장
        giftRepository.save(gift);
        log.info(gift.toString());
        // 2. 저장된 기프티콘에 대해 받은 기프티콘 목록에 추가하기
        GiftBox giftBox = new GiftBox(user, gift);
        giftBoxRepository.save(giftBox);

        gift.changeGiftBox(giftBox);
        giftRepository.save(gift);


        // 3. 맛집 메뉴들의 총액을 계산 후, 결제자의 또페이 잔고에서 출금한다.
        log.info("메뉴 총 금액 : " + dto.getAmount());

        // 해당 유저의 또페이 계정의 잔고에서 출금되는 로직이라고 가정.
        payService.withdrawDdoPay(userId, dto.getAmount());

    }


    @Override
    public void assignment(GiftUpdateRequestDto dto, Long userId) {
        /* 받은 기프티콘을 다른 사람에게 양도할 수 있다. */
        /* 다른 User가 우리 회원인 경우에는 회원이 알아서 조회해야 하나? */
        Gift gift = giftRepository.findById(dto.getGiftId()).orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_GIFTICON));

        // GiftBox가 존재하면 해당 엔티티 삭제
        Optional<GiftBox> giftBox = Optional.ofNullable(gift.getGiftBox());
        if(giftBox.isPresent()) {
            giftBoxRepository.delete(gift.getGiftBox());
        }

        // newUser가 있다면 GiftBox 엔티티 생성 후 저장
        Optional<User> optionalNewUser = userRepository.findByPhoneNum(dto.getPhoneNum());
        if(optionalNewUser.isPresent()) {
            GiftBox newGiftBox = new GiftBox(optionalNewUser.get(), gift);
            giftBoxRepository.save(newGiftBox);
        }
    }

    /**
     * 1. 시큐리티에서 userId 꺼내기
     * 2. userId로 DB 조회, user Entity 조회
     * 3. user.getGiftBoxList()
     * 4. iter giftbox entitiy
     * 5. 만료 기간, 현재시간 비교
     * 6. 상태 변경
     * 7. db 저장
     * 8. 응답
     */
    @Override
    public List<GiftSelectResponseDto> selectMyList(Long userId) {

        // 1. userId로 DB 조회, user Entity 조회
        User findUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));
        // 2. user.getGiftBoxList()
        List<GiftBox> giftBoxList = findUser.getGiftBoxList();

        // 3. iter giftbox entitiy
        List<GiftSelectResponseDto> dtoList = new ArrayList<>();
        for (GiftBox giftBox : giftBoxList) {
            Gift gift = giftBox.getGift();
            // 4. 만료 기간, 현재시간 비교
            if(!isGiftOver(gift)) { // 만료일이 지난 경우
                // 5. 상태 변경
                gift.changeUsedExpired();
                // 6. DB 저장
                giftRepository.save(gift);
            }
            GiftSelectResponseDto dto = GiftSelectResponseDto.from(gift);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public GiftDetailResponseDto selectDetail(int giftId) {
        Gift gift = giftRepository.findById(giftId).orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_GIFTICON));
        return GiftDetailResponseDto.from(gift);
    }

    @Override
    public GiftCheckResponseDto usedCheck(Long userId, GiftCheckRequestDto dto) {
        // 1. 기프티콘 조회
        Gift gift = giftRepository.findById(dto.getGiftId())
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_GIFTICON));

        log.info("요청바디: " + dto);

        // 2. 기프티콘 유효기간 및 사용 가능 여부 확인
        boolean isUsable = isGiftUsable(gift, dto);
        if (!isUsable) {
            throw new CustomException(ResponseCode.GIFT_NOT_USABLE); // 기프티콘 사용 불가 시 CustomException 발생
        }

        // 3. 비밀번호 확인 로직
        if (!payService.verifyGiftPassword(userId, dto.getGiftUsePassword())) {
            throw new CustomException(ResponseCode.INVALID_GIFT_PASSWORD); // 비밀번호 검증 실패 시 CustomException 발생
        }

        // 4. UUID 토큰 생성 및 Redis에 저장 (5분 만료)
        String token = generateUUIDToken(gift, userId);

        return GiftCheckResponseDto.builder()
                .token(token)
                .build();
    }

    // 기프티콘 만료 확인 메서드
    private boolean isGiftOver(Gift gift) {
        return gift.getExpirationDate().isBefore(LocalDateTime.now());
    }

    // 기프티콘 사용 가능 여부 검증 메서드
    private boolean isGiftUsable(Gift gift, GiftCheckRequestDto dto) {
        // 1. 유효기간 만료 확인
        if (isGiftOver(gift)) {
            return false;
        }

        // 2. 연관된 맛집 조회
        Restaurant restaurant = gift.getRestaurant();
        if (restaurant == null) {
            return false;
        }

        // 3. 위치 거리 계산
        double distance = calculateDistance(
                Double.parseDouble(dto.getLatitude()),
                Double.parseDouble(dto.getLongitude()),
                restaurant.getLat(),
                restaurant.getLng()
        );

        // 4. 거리 검증 (반경 15m 이내)
        return distance <= 15.0;
    }

    // Haversine 공식을 이용한 거리 계산 메서드
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // 지구 반경 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c * 1000;
    }

    @Transactional
    public void refund(GiftRefundRequestDto dto, Long userId) {
        // 1. giftId로 기프티콘 조회 (Long 그대로 사용)
        Gift gift = giftRepository.findById(dto.getGiftId())
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_GIFTICON));

        // 2. 기프티콘 소유자(userId) 확인
        if (!gift.getUser().getId().equals(userId)) {
            throw new CustomException(ResponseCode.INVALID_GIFTICON_OWNER);
        }

        // 3. 사용 여부 및 상태 확인
        if (!gift.isRefundable()) { // 예: 이미 사용, 만료, 취소된 경우 등
            throw new CustomException(ResponseCode.NOT_REFUNDABLE_GIFTICON);
        }

        // 4. 상태 변경
        gift.changeUsedCancle();

        // 5. 페이 환불
        payService.depositDdoPay(userId, gift.getAmount());
    }

    // 토큰 생성
    private String generateUUIDToken(Gift gift, Long userId) {
        // UUID를 이용해 토큰 생성
        String token = UUID.randomUUID().toString();

        String key = "token:" + token;
        // 문자열 형태로 Redis에 저장할 값 구성
        String value = "giftId:" + gift.getId() + ",userId:" + userId + ",amount:" + gift.getAmount();

        redisHandler.executeOperation(() ->
                redisHandler.getValueOperations().set(key, value, Duration.ofMinutes(5)));

        return token;
    }


}

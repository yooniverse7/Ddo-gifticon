package com.example.ddo_pay.gift.controller;

import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.common.util.SecurityUtil;
import com.example.ddo_pay.gift.dto.GiftCheckResponseDto;
import com.example.ddo_pay.gift.dto.GiftRefundRequestDto;
import com.example.ddo_pay.gift.dto.GiftSelectResponseDto;
import com.example.ddo_pay.gift.dto.create.GiftCreateRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftCheckRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftDetailResponseDto;
import com.example.ddo_pay.gift.dto.update.GiftUpdateRequestDto;
import com.example.ddo_pay.gift.service.GiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ddo_pay.common.response.ResponseCode.*;


@Slf4j
@RestController
@RequestMapping("/api/gift")
@RequiredArgsConstructor
public class GiftController {


    private final GiftService giftService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCreateRequestDto dto) {

        Long userId = SecurityUtil.getUserId();
        giftService.create(dto, userId);

        return new ResponseEntity<>(Response.create(SUCCESS_CREATE_GIFTICON, null), SUCCESS_CREATE_GIFTICON.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<?> assignment(@RequestBody GiftUpdateRequestDto dto) {

        Long userId = SecurityUtil.getUserId();
        giftService.assignment(dto, userId);

        return new ResponseEntity<>(Response.create(SUCCESS_ASSIGNMENT_GIFTICON, null), SUCCESS_ASSIGNMENT_GIFTICON.getHttpStatus());
    }

    @GetMapping
    public ResponseEntity<?> selectUserGiftList() {
        Long userId = SecurityUtil.getUserId();
        List<GiftSelectResponseDto> dtos = giftService.selectMyList(userId);

        return new ResponseEntity<>(Response.create(SUCCESS_LIST_GIFTICON, dtos), SUCCESS_LIST_GIFTICON.getHttpStatus());
    }

    @GetMapping("/detail/{giftId}")
    public ResponseEntity<?> selectDetail(@PathVariable int giftId) {
        GiftDetailResponseDto dto = giftService.selectDetail(giftId);
        return new ResponseEntity<>(Response.create(SUCCESS_DETAIL_GIFTICON, dto), SUCCESS_DETAIL_GIFTICON.getHttpStatus());
    }

    // 특정 기프티콘의 사용 가능 여부를 알 수 있다.
    @PostMapping("/check")
    public ResponseEntity<?> usedCheck(@RequestBody GiftCheckRequestDto dto) {
        Long userId = SecurityUtil.getUserId();
        log.info("요청바디{}", dto);
        GiftCheckResponseDto respDto = giftService.usedCheck(userId, dto);
        return new ResponseEntity<>(Response.create(SUCCESS_CHECK_GIFTICON, respDto), SUCCESS_CHECK_GIFTICON.getHttpStatus());
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestBody GiftRefundRequestDto dto) {
        Long userId = SecurityUtil.getUserId();
        giftService.refund(dto, userId);
        return new ResponseEntity<>(Response.create(SUCCESS_REFUND_GIFT, null), SUCCESS_REFUND_GIFT.getHttpStatus());
    }


}


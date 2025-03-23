package com.example.ddo_pay.gift.controller;

import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.gift.dto.GiftCheckResponseDto;
import com.example.ddo_pay.gift.dto.GiftSelectResponseDto;
import com.example.ddo_pay.gift.dto.create.GiftCreateRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftCheckRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftDetailResponseDto;
import com.example.ddo_pay.gift.dto.update.GiftUpdateRequestDto;
import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.service.GiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.ddo_pay.common.response.ResponseCode.*;


@RestController
@RequestMapping("/api/gift")
@RequiredArgsConstructor
public class GiftController {


    private final GiftService giftService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCreateRequestDto dto) {

        giftService.create(dto);

        return new ResponseEntity<>(Response.create(SUCCESS_CREATE_GIFTICON, null), SUCCESS_CREATE_GIFTICON.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<?> assignment(@RequestBody GiftUpdateRequestDto dto) {

        giftService.assignment(dto);

        return new ResponseEntity<>(Response.create(SUCCESS_ASSIGNMENT_GIFTICON, null), SUCCESS_ASSIGNMENT_GIFTICON.getHttpStatus());
    }

    @GetMapping
    public ResponseEntity<?> selectMyList() {
        List<GiftSelectResponseDto> dtos = giftService.selectMyList();

        return new ResponseEntity<>(Response.create(SUCCESS_LIST_GIFTICON, dtos), SUCCESS_LIST_GIFTICON.getHttpStatus());
    }

    @GetMapping("/detail/${giftId}")
    public ResponseEntity<?> selectDetail(@PathVariable int giftId) {
        GiftDetailResponseDto dto = giftService.selectDetail(giftId);
        return new ResponseEntity<>(Response.create(SUCCESS_DETAIL_GIFTICON, dto), SUCCESS_DETAIL_GIFTICON.getHttpStatus());
    }

    @PostMapping("/check")
    public ResponseEntity<?> usedCheck(@RequestBody GiftCheckRequestDto dto) {
        GiftCheckResponseDto respDto = giftService.usedCheck(dto);
        return new ResponseEntity<>(Response.create(SUCCESS_CHECK_GIFTICON, respDto), SUCCESS_CHECK_GIFTICON.getHttpStatus());
    }

}

package com.example.ddo_pay.gift.service;

import com.example.ddo_pay.gift.dto.GiftCheckResponseDto;
import com.example.ddo_pay.gift.dto.GiftRefundRequestDto;
import com.example.ddo_pay.gift.dto.GiftSelectResponseDto;
import com.example.ddo_pay.gift.dto.create.GiftCreateRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftCheckRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftDetailResponseDto;
import com.example.ddo_pay.gift.dto.update.GiftUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiftService {
    // 기프티콘 발행하기
    void create(GiftCreateRequestDto dto, Long userId);

    // 기프티콘 양도하기
    void assignment(GiftUpdateRequestDto dto, Long userId);

    // 받은 기프티콘 리스트 조회하기
    List<GiftSelectResponseDto> selectMyList(Long userId);

    // giftId를 가진 기프티콘 상세보기
    GiftDetailResponseDto selectDetail(int giftId);

    // 해당 기프티콘의 사용여부 확인하기
    GiftCheckResponseDto usedCheck(Long userId, GiftCheckRequestDto dto);

    void refund(GiftRefundRequestDto dto, Long userId);
}

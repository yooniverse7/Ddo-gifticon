package com.example.ddo_pay.gift.service;

import com.example.ddo_pay.gift.dto.GiftCheckResponseDto;
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
    public void create(GiftCreateRequestDto dto);

    // 기프티콘 양도하기
    public void assignment(GiftUpdateRequestDto dto);

    // 받은 기프티콘 리스트 조회하기
    public List<GiftSelectResponseDto> selectMyList();

    // giftId를 가진 기프티콘 상세보기
    public GiftDetailResponseDto selectDetail(int giftId);

    // 해당 기프티콘의 사용여부 확인하기
    public GiftCheckResponseDto usedCheck(GiftCheckRequestDto dto);

}

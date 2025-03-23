package com.example.ddo_pay.gift.service;

import com.example.ddo_pay.gift.Repository.GiftRepository;
import com.example.ddo_pay.gift.dto.GiftCheckResponseDto;
import com.example.ddo_pay.gift.dto.GiftSelectResponseDto;
import com.example.ddo_pay.gift.dto.create.GiftCreateRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftCheckRequestDto;
import com.example.ddo_pay.gift.dto.select.GiftDetailResponseDto;
import com.example.ddo_pay.gift.dto.update.GiftUpdateRequestDto;
import com.example.ddo_pay.gift.entity.Gift;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {

    private final GiftRepository giftRepository;

    @Override
    public void create(GiftCreateRequestDto dto) {
        Gift gift = Gift.builder()
                .title(dto.getGiftTitle())
                .amount(dto.getAmount())
                .phoneNum(dto.getPhoneNum())
                .message(dto.getMessage())
                .image(dto.getImage())
                .build();

        giftRepository.save(gift);
    }

    @Override
    public void assignment(GiftUpdateRequestDto dto) {
        Gift gift = giftRepository.findById(dto.getGiftId()).orElseThrow();
        gift.changePhoneNum(dto.getPhoneNum());
    }

    @Override
    public List<GiftSelectResponseDto> selectMyList() {
        Long userId = 1L;
        List<Gift> gifts = giftRepository.selectMyList(userId);
        return gifts.stream().map(GiftSelectResponseDto::from).toList();
    }

    @Override
    public GiftDetailResponseDto selectDetail(int giftId) {
        return null;
    }

    @Override
    public GiftCheckResponseDto usedCheck(GiftCheckRequestDto dto) {
        return null;
    }
}

package com.example.ddo_pay.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ddo_pay.user.dto.UserDto;
import com.example.ddo_pay.user.dto.request.SocialLoginRequestDto;
import com.example.ddo_pay.user.dto.response.SocialLoginResponseDto;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.mapper.UserMapper;
import com.example.ddo_pay.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public SocialLoginResponseDto socialUserLogin(SocialLoginRequestDto reqDto) {
        // DB에서 유저 검색
        Optional<User> userDto = userRepo.findByLoginId(reqDto.getSocialId());

        // 유저 검색되지 않음 유저와 비교
        if (userDto.isEmpty()) {
        } else {
            log.debug("userDto is finded");
        }

        // response 생성
        SocialLoginResponseDto respDto = new SocialLoginResponseDto();
        respDto.setAccessToken("acc-tkn");
        respDto.setRefreshToken("ref-tkn");

        return respDto;
    }

    @Override
    public UserDto getUserInfo(UserDto reqDto) {
        Optional<User> targetUser = userRepo.findById(reqDto.getUserId());
        if (targetUser.isEmpty()) {
            log.info("Fail GetUser. No UserId in DB");
        }

        UserDto respDto = userMapper.fromUserEntity(targetUser.get());

        return respDto;
    }

    @Override
    public void changeUserInfo(UserDto reqDto) {
        Optional<User> targetUser = userRepo.findById(reqDto.getUserId());
        if (targetUser.isEmpty()) {
            log.info("Fail GetUser. No UserId in DB");
        }

        User user = targetUser.get();
        user.changePrivateInfo(reqDto);
        userRepo.save(user);
    }

    @Override
    public void logoutUser(UserDto reqDto) {
        // logout logic

    }

}

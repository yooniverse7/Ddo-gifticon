package com.example.ddo_pay.user.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Named;

import com.example.ddo_pay.user.dto.UserDto;
import com.example.ddo_pay.user.dto.request.UserInfoRequestDto;
import com.example.ddo_pay.user.dto.response.UserInfoResponseDto;
import com.example.ddo_pay.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    // @Mapping(source=”source 필드명”, target=”target 필드명”)
    // UserDto를 기준으로 합니다

    /**
     * UserDto -> UserInfoResponseDto
     * 
     * @param userDto
     * @return UserInfoResponseDto
     */
    @Mapping(source = "name", target = "userName")
    UserInfoResponseDto toUserInfoResponseDto(UserDto userDto);

    /**
     * UserInfoRequestDto -> UserDto
     * 
     * @param UserInfoRequestDto
     * @return UserDto
     */
    @Mapping(source = "userEmail", target = "email")
    UserDto fromUserInfoRequestDto(UserInfoRequestDto userInfoRequestDto);

    /**
     * User(Entity) -> UserDto
     * 
     * @param User (Entity)
     * @return UserDto
     */
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "birthday", target = "birth", qualifiedByName = "time2str")
    UserDto fromUserEntity(User user);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "birth", target = "birthday", qualifiedByName = "str2time")
    User toUserEntity(UserDto userDto);

    // LocalDateTime -> String 변환 메서드
    @Named("time2str")
    default String mapLocalDateTimeToString(LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE); // 원하는 포맷으로 변경
        }
        return null;
    }

    // String -> LocalDateTime 변환 메서드
    @Named("str2time")
    default LocalDateTime mapStringToLocalDateTime(String dateTimeString) {
        if (dateTimeString != null && !dateTimeString.isEmpty()) {
            return LocalDateTime.parse(dateTimeString + "T00:00:00",
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return null;
    }
}

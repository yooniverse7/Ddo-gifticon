package com.example.ssafy_bank.bank.dto.finance_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateAccountResponseDto {

    @JsonProperty("Header")
    private Header header;

    @JsonProperty("REC")
    private Rec rec;

    @Getter
    @Setter
    public static class Header {
        private String responseCode;
        private String responseMessage;
        private String apiName;
        private String transmissionDate;
        private String transmissionTime;
        private String institutionCode;
        private String apiKey;
        private String apiServiceCode;
        private String institutionTransactionUniqueNo;
    }

    @Getter
    @Setter
    public static class Rec {
        private String bankCode;
        private String accountNo;
    }
}

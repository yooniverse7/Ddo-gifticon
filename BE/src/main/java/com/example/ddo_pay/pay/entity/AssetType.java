package com.example.ddo_pay.pay.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetType {

     BALANCE(1), POINT(2);

    private final int type;
}

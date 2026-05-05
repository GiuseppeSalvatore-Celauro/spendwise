package com.celauro.SpendWise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtDTO {
    String token;
    Date expDate;
}

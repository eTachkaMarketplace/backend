package com.sellbycar.marketplace.rest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {

  private final String type = "Bearer";
  private String jwtAccessToken;

  private String jwtRefreshToken;

}

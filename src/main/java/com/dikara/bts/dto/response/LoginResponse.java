package com.dikara.bts.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {


    private String token;
    private String refreshToken;
    private String username;
    private String role;

}

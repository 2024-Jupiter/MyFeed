package com.myfeed.jwt;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
}

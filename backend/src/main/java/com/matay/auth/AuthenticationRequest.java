package com.matay.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}

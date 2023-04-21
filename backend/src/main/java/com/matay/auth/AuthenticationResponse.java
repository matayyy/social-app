package com.matay.auth;

import com.matay.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO) {
}

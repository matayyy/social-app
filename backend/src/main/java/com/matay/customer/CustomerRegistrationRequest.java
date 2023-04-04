package com.matay.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,
        Gender gender
) {

}

package com.matay.customer;

import com.matay.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.save(customer);

        //when
        boolean actual = underTest.existsCustomerByEmail(email);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //when
        boolean actual = underTest.existsCustomerByEmail(email);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //when
        boolean actual = underTest.existsCustomerById(id);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        //given
        int id = -1;

        //when
        boolean actual = underTest.existsCustomerById(id);

        //then
        assertThat(actual).isFalse();
    }
}
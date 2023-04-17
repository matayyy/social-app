package com.matay.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //when
        underTest.selectAllCustomers();

        //then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //given
        int id = 1;

        //then
        underTest.selectCustomerById(id);

        //when
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //given
        Customer customer = new Customer(
                1, "Gabe", "gabe@gmail.com", "password ", 14,
                Gender.MALE);

        //when
        underTest.insertCustomer(customer);

        //then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        //given
        String email = "testing.email@email.com";

        //when
        underTest.existsCustomerWithEmail(email);

        //then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerWithId() {
        //given
        int id = 1;

        //when
        underTest.existsCustomerWithId(id);

        //then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //given
        int id = 1;

        //when
        underTest.deleteCustomerById(id);

        //then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //given
        Customer customer = new Customer(
                1, "Gabe", "gabe@gmail.com", "password ", 14,
                Gender.MALE);

        //when
        underTest.updateCustomer(customer);

        //then
        verify(customerRepository).save(customer);
    }
}
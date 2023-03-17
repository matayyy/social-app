package com.matay.customer;

import com.matay.exception.DuplicateResourceException;
import com.matay.exception.RequestValidationException;
import com.matay.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //when
        underTest.getAllCustomers();

        //then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@mail.com", 32
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        Customer actual = underTest.getCustomer(id);

        //then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //given
        int id = 1;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //given
        String email = "alex@mail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 24
        );

        //when
        underTest.addCustomer(request);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        //given
        String email = "alex@mail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 24
        );

        //when
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        //given
        int id = 1;

        when(customerDao.existsCustomerWithId(id)).thenReturn(true);

        //when
        underTest.deleteCustomerById(id);

        //then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenUserNotFoundWhileDeletingCustomer() {
        //given
        int id = 1;

        when(customerDao.existsCustomerWithId(id)).thenReturn(false);

        //when
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        //then
        verify(customerDao, never()).deleteCustomerById(any());
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "gabe", "gabe@mail.com", 24
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "gabriel@mail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Gabriel", newEmail, 25);

        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        //when
        underTest.updateCustomer(id, updateRequest);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "gabe", "gabe@mail.com", 24
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Gabriel", null, null);

        //when
        underTest.updateCustomer(id, updateRequest);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "gabe", "gabe@mail.com", 24
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 55);

        //when
        underTest.updateCustomer(id, updateRequest);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "gabe", "gabe@mail.com", 24
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "gabriel@mail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        //when
        underTest.updateCustomer(id, updateRequest);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "gabe", "gabe@mail.com", 24
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "gabriel@mail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Gabriel", newEmail, 25);

        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(true);

        //when
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerHasNoChanges() {
        //given
        int id = 1;
        Customer customer = new Customer(
                id, "gabe", "gabe@mail.com", 24
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, null);

        //when
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //then
        verify(customerDao, never()).updateCustomer(any());
    }
}
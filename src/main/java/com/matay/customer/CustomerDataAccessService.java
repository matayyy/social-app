package com.matay.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao {

    //db
    private static final List<Customer> customers = new ArrayList<>();
    static {
        customers.add(new Customer(1, "Gabi", "gabi@mail.com", 24));
        customers.add(new Customer(2, "Caroline", "caroline@mail.com", 24));
        customers.add(new Customer(3, "Martha", "martha@mail.com", 24));
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }
}

package com.matay;

import com.matay.customer.Customer;
import com.matay.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner (CustomerRepository customerRepository) {
        return args -> {
            List<Customer> customers = new ArrayList<>();
            customers.add(new Customer("Gabi", "gabi@mail.com", 24));
            customers.add(new Customer("Caroline", "caroline@mail.com", 24));
            customers.add(new Customer("Martha", "martha@mail.com", 24));
            customerRepository.saveAll(customers);
        };
    }
}

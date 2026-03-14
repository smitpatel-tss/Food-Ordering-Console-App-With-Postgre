package com.tss.repositories;

import com.tss.model.users.Customer;

import java.util.List;

public interface CustomerRepo {
    void addNewCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(long id);
}

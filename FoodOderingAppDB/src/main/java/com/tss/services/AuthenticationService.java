package com.tss.services;

import com.tss.exceptions.UserNotFoundException;
import com.tss.model.users.*;
import com.tss.repositories.UserRepository;

public class AuthenticationService {

    private UserService userService = UserService.getInstance();

    private User authenticate() {
        return userService.authenticateUser();
    }

    public Admin loginAdmin() {
        User user = authenticate();
        if (!(user instanceof Admin)) {
            throw new UserNotFoundException("Admin not found!");
        }
        return (Admin) user;
    }

    public Customer loginCustomer() {
        User user = authenticate();
        if (!(user instanceof Customer)) {
            throw new UserNotFoundException("Customer not found!");
        }
        return (Customer) user;
    }

    public DeliveryPartner loginDeliveryPartner() {
        User user = authenticate();
        if (!(user instanceof DeliveryPartner)) {
            throw new UserNotFoundException("Delivery Partner not found!");
        }
        return (DeliveryPartner) user;
    }

    public void newCustomerRegister() {

        User customer = userService.makeUser(UserType.CUSTOMER);

        UserRepository.getInstance().addUser(customer);

        System.out.println("✔ New Customer " + customer.getName()
                + " Registered, with Phone Number " + customer
                .getAccountInfo().getPhoneNumber());
    }
}
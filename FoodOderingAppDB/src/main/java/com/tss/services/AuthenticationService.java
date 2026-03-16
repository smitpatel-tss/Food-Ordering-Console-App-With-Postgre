package com.tss.services;

import com.tss.exceptions.UserNotFoundException;
import com.tss.model.users.*;
import com.tss.repositories.CustomerRepo;
import com.tss.repositories.CustomerRepoImpl;

public class AuthenticationService {

    private UserService userService;
    private CustomerRepo customerRepo;

    public AuthenticationService() {
        userService=UserService.getInstance();
        customerRepo=new CustomerRepoImpl();
    }

    private User authenticate(UserType type) {
        return userService.authenticateUser(type);
    }

    public Admin loginAdmin() {
        User user = authenticate(UserType.ADMIN);
        if (!(user instanceof Admin)) {
            throw new UserNotFoundException("Admin not found!");
        }
        return (Admin) user;
    }

    public Customer loginCustomer() {
        User user = authenticate(UserType.CUSTOMER);
        if (!(user instanceof Customer)) {
            throw new UserNotFoundException("Customer not found!");
        }
        return (Customer) user;
    }

    public DeliveryPartner loginDeliveryPartner() {
        User user = authenticate(UserType.DELIVERY_PARTNER);
        if (!(user instanceof DeliveryPartner)) {
            throw new UserNotFoundException("Delivery Partner not found!");
        }
        return (DeliveryPartner) user;
    }

    public void newCustomerRegister() {

        User customer = userService.makeUser(UserType.CUSTOMER);

        customerRepo.addNewCustomer((Customer) customer);

        System.out.println("✔ New Customer " + customer.getName()
                + " Registered, with Phone Number " + customer
                .getAccountInfo().getPhoneNumber());
    }
}
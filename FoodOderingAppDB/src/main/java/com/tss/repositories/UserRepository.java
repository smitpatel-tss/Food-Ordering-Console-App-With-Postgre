package com.tss.repositories;

import com.tss.model.users.Admin;
import com.tss.model.users.Customer;
import com.tss.model.users.DeliveryPartner;
import com.tss.model.users.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users;

    private UserRepository() {
        users = new ArrayList<>();
    }

    private static class RepoContainer {
        static UserRepository obj = new UserRepository();
    }

    public static UserRepository getInstance() {
        return RepoContainer.obj;
    }

    public List<DeliveryPartner> getDeliveryPartners() {
        List<DeliveryPartner> deliveryPartners = new ArrayList<>();
        for (User user : users) {
            if (user instanceof DeliveryPartner) {
                deliveryPartners.add((DeliveryPartner) user);
            }
        }
        return deliveryPartners;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
        return customers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User newUser) {
        users.add(newUser);
    }

    public User getUserFromNumber(long number) {
        for (User user : users) {
            if (user.getAccountInfo().getPhoneNumber()==number) {
                return user;
            }
        }
        return null;
    }

    public boolean canAddNumber(long number) {
        if (getUserFromNumber(number) == null) {
            return true;
        }
        return false;
    }

    public boolean passwordCheck(long number, String password) {
        User user = getUserFromNumber(number);
        if (user == null) {
            return false;
        }
        if (user.getAccountInfo().getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public Customer getUserFromId(long id) {
        for (User user : users) {
            if (user.getId() == id && user instanceof Customer) {
                return (Customer) user;
            }
        }
        return null;
    }

    public Admin getAdmin() {
        for (User user : users) {
            if (user instanceof Admin) {
                return (Admin) user;
            }
        }
        return null;
    }

}

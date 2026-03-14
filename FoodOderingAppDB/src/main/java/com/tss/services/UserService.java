package com.tss.services;

import com.tss.authentication.AccountInfo;
import com.tss.factory.UserFactory;
import com.tss.model.Notification;
import com.tss.model.users.User;
import com.tss.model.users.UserType;
import com.tss.repositories.UserRepository;
import com.tss.utils.Validate;

import java.util.List;

public class UserService {
    private UserFactory userFactory;
    private UserRepository userRepository;
    private NotificationService notificationService;

    private UserService() {
        userFactory = new UserFactory();
        userRepository = UserRepository.getInstance();
        notificationService = NotificationService.getInstance();
    }

    private static class InstanceContainer {
        static UserService obj = new UserService();
    }

    public static UserService getInstance() {
        return UserService.InstanceContainer.obj;
    }

    public User makeUser(UserType type) {
        System.out.println("\n=================================");
        System.out.println("        CREATE NEW ACCOUNT       ");
        System.out.println("=================================");

        System.out.print("Name          : ");
        String name = Validate.validateCharOnlyString();

        System.out.print("Phone Number  : ");
        long number = Validate.validatePhoneNumber();
        while (!userRepository.canAddNumber(number)) {
            System.out.println("✖ Number already registered.");
            System.out.print("Enter Different Number : ");
            number = Validate.validatePhoneNumber();
        }

        System.out.print("Password      : ");
        String password = Validate.validatePassword();

        return userFactory.getUser(name, type, new AccountInfo(number, password));
    }

    public User authenticateUser() {
        System.out.println("\n=================================");
        System.out.println("             LOGIN               ");
        System.out.println("=================================");

        System.out.print("Enter Phone number: ");
        long number = Validate.validatePhoneNumber();
        System.out.print("Enter Password    : ");
        String password = Validate.validatePassword();

        User user = userRepository.getUserFromNumber(number);

        if (user == null) {
            return null;
        }
        if (userRepository.passwordCheck(number, password)) {
            return user;
        }

        return null;
    }

    public void changePassword(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        System.out.print("Enter Current Password : ");
        String oldPassword = Validate.validatePassword();

        if (!userRepository.passwordCheck(user.getAccountInfo().getPhoneNumber(), oldPassword)) {
            System.out.println("✖ Incorrect Password.");
            return;
        }
        System.out.print("Enter New Password     : ");
        String newPassword = Validate.validatePassword();

        while (user.getAccountInfo().getPassword().equals(newPassword)) {
            System.out.println("New password cannot be same as old password.");
            System.out.print("Enter different Password: ");
            newPassword = Validate.validatePassword();
        }
        System.out.println("Confirm password change? (Y/N) : ");
        if (!Validate.validateYesNo()) {
            return;
        }
        user.getAccountInfo().setPassword(newPassword);
        System.out.println("✔ Password Changed Successfully.");
    }

    public void changeNumber(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        System.out.print("Enter Phone number: ");
        long number = Validate.validatePhoneNumber();
        while (!userRepository.canAddNumber(number)) {
            if (user.getAccountInfo().getPhoneNumber()==number) {
                System.out.println("✖ This number is already linked to your account.");
                return;
            }
            System.out.println("Number already registered!");
            System.out.print("Enter Different Number: ");
            number = Validate.validatePhoneNumber();
        }
        System.out.println("Confirm phone number change? (Y/N) : ");
        if (!Validate.validateYesNo()) {
            return;
        }
        user.getAccountInfo().setPhoneNumber(number);
        System.out.println("✔ Phone Number Updated Successfully.");
    }

    public void displayUserNotifications(User user) {
        List<Notification> notifications = notificationService.getNotifications(user.getId());
        if (notifications.isEmpty()) {
            System.out.println("No Notification yet!");
            return;
        }
        System.out.println("INBOX:");
        notificationService.displayNotifications(notifications);
        notificationService.clearNotifications(user.getId());
    }
}

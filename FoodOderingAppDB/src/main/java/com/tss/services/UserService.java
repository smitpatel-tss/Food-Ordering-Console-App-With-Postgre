package com.tss.services;

import com.tss.authentication.AccountInfo;
import com.tss.factory.UserFactory;
import com.tss.model.Notification;
import com.tss.model.users.User;
import com.tss.model.users.UserType;
import com.tss.repositories.*;
import com.tss.utils.Validate;

import java.util.List;

public class UserService {
    private UserFactory userFactory;
    private NotificationService notificationService;
    private NotificationRepo notificationRepo;
    private UserRepo userRepo;

    private UserService() {
        userFactory = new UserFactory();
        notificationService = NotificationService.getInstance();
        notificationRepo=new NotificationRepoImpl();
        userRepo=new UserRepoImpl();
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
        while (!userRepo.canAddNumber(number,type)) {
            System.out.println("Number already registered.");
            System.out.print("Enter Different Number : ");
            number = Validate.validatePhoneNumber();
        }

        System.out.print("Password      : ");
        String password = Validate.validatePassword();

        return userFactory.getUser(name, type, new AccountInfo(number, password));
    }

    public User authenticateUser(UserType type) {
        System.out.println("\n=================================");
        System.out.println("             LOGIN               ");
        System.out.println("=================================");

        System.out.print("Enter Phone number: ");
        long number = Validate.validatePhoneNumber();
        System.out.print("Enter Password    : ");
        String password = Validate.validatePassword();

        User user = userRepo.getUserFromNumber(number,type);

        if (user == null) {
            return null;
        }
        if (userRepo.checkPassword(number, password,type)) {
            return user;
        }

        return null;
    }

    public void changePassword(User user,UserType type) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        System.out.print("Enter Current Password : ");
        String oldPassword = Validate.validatePassword();

        if (!userRepo.checkPassword(user.getAccountInfo().getPhoneNumber(),oldPassword,type)) {
            System.out.println("Incorrect Password.");
            return;
        }
        System.out.print("Enter New Password     : ");
        String newPassword = Validate.validatePassword();

        while (oldPassword.equals(newPassword)) {
            System.out.println("New password cannot be same as old password.");
            System.out.print("Enter different Password: ");
            newPassword = Validate.validatePassword();
        }
        System.out.println("Confirm password change? (Y/N) : ");
        if (!Validate.validateYesNo()) {
            return;
        }
        user.getAccountInfo().setPassword(newPassword);
        userRepo.changePassword(user.getId(),newPassword,type);
        System.out.println("Password Changed Successfully.");
    }

    public void changeNumber(User user,UserType type) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        System.out.print("Enter Phone number: ");
        long number = Validate.validatePhoneNumber();
        while (!userRepo.canAddNumber(number,type)) {
            if (user.getAccountInfo().getPhoneNumber()==number) {
                System.out.println("This number is already linked to your account.");
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
        userRepo.changePhoneNumber(user.getId(),number,type);
        System.out.println("Phone Number Updated Successfully.");
    }

    public void displayUserNotifications(User user) {
        List<Notification> notifications = notificationRepo.getAllUnseenNotifications(user.getId());
        if (notifications.isEmpty()) {
            System.out.println("No Notification yet!");
            return;
        }
        System.out.println("INBOX:");
        notificationService.displayNotifications(notifications);
    }
}

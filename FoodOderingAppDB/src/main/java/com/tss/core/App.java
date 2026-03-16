package com.tss.core;

import com.tss.authentication.AccountInfo;
import com.tss.controllers.*;
import com.tss.factory.UserFactory;
import com.tss.model.users.Admin;
import com.tss.model.users.UserType;
import com.tss.repositories.UserRepoImpl;
import com.tss.utils.Validate;

public class App {

    public void start() {

        adminInitializer();

        AdminPanel adminPanel = new AdminPanel();
        CustomerPanel customerPanel = new CustomerPanel();
        DeliveryPartnerPanel deliveryPartnerPanel = new DeliveryPartnerPanel();

        System.out.println("WELCOME TO OUR APP");

        while (true) {

            try{
                ControlPanelDisplay.displayMainMenu();
                System.out.print("Choose option: ");
                int choice = Validate.validateIntLimit(3);

                switch (choice) {
                    case 1 -> adminPanel.start();
                    case 2 -> customerPanel.start();
                    case 3 -> deliveryPartnerPanel.start();
                    case 0 -> {
                        System.out.println("EXITING...");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Something Went Wrong!");
            }
        }
    }

    private void adminInitializer() {

        UserFactory factory = new UserFactory();
        Admin admin = (Admin) factory.getUser(
                "Smit", UserType.ADMIN,
                new AccountInfo(7069209810L, "smit1234"));

        new UserRepoImpl().addAdmin(admin);
    }
}
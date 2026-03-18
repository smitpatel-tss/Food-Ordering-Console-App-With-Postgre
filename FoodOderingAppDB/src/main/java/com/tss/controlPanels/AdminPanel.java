package com.tss.controlPanels;

import com.tss.exceptions.ItemNotFoundException;
import com.tss.exceptions.UserNotFoundException;
import com.tss.model.users.Admin;
import com.tss.services.AdminService;
import com.tss.services.AuthenticationService;
import com.tss.utils.Validate;

public class AdminPanel {

    private final AuthenticationService authService;

    public AdminPanel() {
        this.authService = new AuthenticationService();
    }

    public void start() {

        try {
            Admin admin = authService.loginAdmin();

            AdminService adminService =
                    new AdminService(admin);

            adminService.welcomeDisplay();
            adminMenu(adminService);

        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void menuManagement(AdminService adminService) {

        while (true) {
            ControlPanelDisplay.displayMenuManagementMenu();
            System.out.print("Choose option: ");
            int choice = Validate.validateIntLimit(6);

            switch (choice) {
                case 1 -> adminService.displayMenu();
                case 2 -> adminService.addCuisineType();
                case 3 -> adminService.addNewFoodItem();
                case 4 -> {
                    try {
                        adminService.changePrice();
                    } catch (ItemNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> adminService.removeItem();
                case 6 -> adminService.removeCuisine();
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
            }
        }
    }

    public void orderAndDiscountManagement(AdminService adminService) {

        while (true) {
            ControlPanelDisplay.displayOrderAndDiscountMenu();
            System.out.print("Choose option : ");
            int choice = Validate.validateIntLimit(5);

            switch (choice) {
                case 1 -> adminService.printAllOrders();
                case 2 -> adminService.displayPendingOrders();
                case 3 -> adminService.revenueDetails();
                case 4 -> adminService.addDiscount();
                case 5 -> adminService.displayDiscounts();
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
            }
        }
    }

    public void userManagement(AdminService adminService) {

        while (true) {
            ControlPanelDisplay.displayUserManagementMenu();
            System.out.print("Choose option : ");
            int choice = Validate.validateIntLimit(3);

            switch (choice) {
                case 1 -> adminService.addNewDeliveryPartner();
                case 2 -> adminService.showDeliveryPartners();
                case 3 -> adminService.displayAllCustomers();
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
            }
        }
    }

    public void notificationManagement(AdminService adminService) {

        while (true) {
            ControlPanelDisplay.displayNotificationManagementMenu();
            System.out.print("Choose option : ");
            int choice = Validate.validateIntLimit(3);

            switch (choice) {
                case 1 -> {
                    try {
                        adminService.sendNotificationToCustomers();
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        adminService.sendNotificationToDeliveryPartners();
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        adminService.displayNotifications();
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
            }
        }
    }

    public void manageCredentials(AdminService adminService) {

        while (true) {
            ControlPanelDisplay.displayCredentialManagementMenu();
            System.out.print("Choose option : ");
            int choice = Validate.validateIntLimit(2);

            switch (choice) {
                case 1 -> {
                    try {
                        adminService.changePhoneNumber();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        adminService.changePassword();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
            }
        }
    }

    public void adminMenu(AdminService adminService) {

        while (true) {
            ControlPanelDisplay.displayAdminMainMenuNew();
            System.out.print("Choose option : ");
            int choice = Validate.validateIntLimit(5);
            switch (choice) {

                case 1 -> menuManagement(adminService);
                case 2 -> orderAndDiscountManagement(adminService);
                case 3 -> userManagement(adminService);
                case 4 -> notificationManagement(adminService);
                case 5 -> manageCredentials(adminService);

                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
            }
        }
    }
}
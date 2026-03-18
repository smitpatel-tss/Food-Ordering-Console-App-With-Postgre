package com.tss.controlPanels;

import com.tss.exceptions.CartEmptyException;
import com.tss.exceptions.ItemNotFoundException;
import com.tss.exceptions.UserNotFoundException;
import com.tss.model.users.Customer;
import com.tss.services.AuthenticationService;
import com.tss.services.CustomerService;
import com.tss.utils.Validate;

public class CustomerPanel {

    private final AuthenticationService authService;

    public CustomerPanel() {
        this.authService = new AuthenticationService();
    }

    public void start() {

        while (true) {

            ControlPanelDisplay.displayUserFirstMenu();
            System.out.print("Choose Option From Menu: ");
            int choice = Validate.validateIntLimit(2);

            switch (choice) {

                case 1 -> {
                    authService.newCustomerRegister();
                }

                case 2 -> {
                    logInToMainMenu();
                }

                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
            }
        }
    }


    private void logInToMainMenu() {
        try {
            Customer customer = authService.loginCustomer();
            CustomerService customerService = new CustomerService(customer);

            customerService.welcomeDisplay();

            while (true) {
                ControlPanelDisplay.displayCustomerMainMenu();
                System.out.print("Choose option : ");

                int choice = Validate.validateIntLimit(3);

                switch (choice) {
                    case 1 -> ordersManagement(customerService);
                    case 2 -> historyAndNotifications(customerService);
                    case 3 -> accountAndSupport(customerService);
                    case 0 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid option!");
                }
            }

        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    private void ordersManagement(CustomerService customerService) {

        while (true) {
            ControlPanelDisplay.displayOrdersManagementMenu();
            System.out.print("Choose option : ");

            int choice = Validate.validateIntLimit(6);

            switch (choice) {
                case 1 -> {
                    try {
                        customerService.addItemToCart();
                    } catch (ItemNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        customerService.displayCart();
                        customerService.removeItemFromCart();
                    } catch (CartEmptyException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try{
                        customerService.displayCart();
                    }catch (CartEmptyException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> {
                    try {
                        customerService.placeOrder();
                    } catch (CartEmptyException e) {
                        System.out.println(e.getMessage());
                    }catch (IllegalArgumentException e){
                        System.out.println("Payment Error: "+e.getMessage());
                    }
                }
                case 5 -> customerService.displayMenu();
                case 6 -> customerService.displayDiscounts();
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void historyAndNotifications(CustomerService customerService) {

        while (true) {
            ControlPanelDisplay.displayHistoryNotificationsMenu();
            System.out.print("Choose option : ");

            int choice = Validate.validateIntLimit(2);

            switch (choice) {
                case 1 -> customerService.showHistory();
                case 2 -> customerService.displayNotifications();
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void accountAndSupport(CustomerService customerService) {

        while (true) {
            ControlPanelDisplay.displayAccountSupportMenu();
            System.out.print("Choose option : ");

            int choice = Validate.validateIntLimit(3);

            switch (choice) {
                case 1 -> {
                    try {
                        customerService.changePhoneNumber();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        customerService.changePassword();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> customerService.customerSupport();
                case 0 -> {
                    System.out.println("Back...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

}
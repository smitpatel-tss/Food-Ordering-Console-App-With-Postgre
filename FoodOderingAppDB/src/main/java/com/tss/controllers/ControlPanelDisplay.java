package com.tss.controllers;

public class ControlPanelDisplay {
    public static void displayMainMenu() {
        System.out.println("""
                \n------------------------------------------------------
                => MAIN MENU:
                ------------------------------------------------------
                1. Admin
                2. Customer
                3. Delivery Partner
                0. Exit""");
        System.out.println("\u2500".repeat(60));

    }

    public static void displayDeliveryPartnerMenu() {
        System.out.println("""
                \n=> DELIVERY PARTNER MAIN MENU:
                ------------------------------------------------------
                1. Delivery Confirmation
                2. Order History
                3. Notification Inbox
                4. Update Phone Number
                5. Change Password
                6. Report Issue
                0. LogOut""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayMenuManagementMenu() {
        System.out.println("""
                \n=> MENU MANAGEMENT:
                ------------------------------------------------------
                1. Display Menu
                2. Add new Cuisine Type
                3. Add new Food Item
                4. Change Price
                5. Remove Item from Menu
                6. Remove Cuisine Type
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayUserManagementMenu() {
        System.out.println("""
                \n=> CUSTOMERS & DELIVERY PARTNER MANAGEMENT:
                ------------------------------------------------------
                1. Add New Delivery Partner
                2. Display Delivery Partners
                3. Display All Customers
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayOrderAndDiscountMenu() {
        System.out.println("""
                \n=> ORDER & DISCOUNT MANAGEMENT:
                ------------------------------------------------------
                1. Display Orders History
                2. Display Pending Orders
                3. Sales Summary
                4. Add Discount
                5. Display Our Discounts
                0. Back""");
        System.out.println("\u2500".repeat(60));

    }

    public static void displayNotificationManagementMenu() {
        System.out.println("""
                \n=> NOTIFICATION MANAGEMENT:
                ------------------------------------------------------
                1. Broadcast Notification to all Customers
                2. Broadcast Notification to all Delivery Partners
                3. Notification Inbox
                0. Back""");
        System.out.println("\u2500".repeat(60));

    }

    public static void displayCredentialManagementMenu() {
        System.out.println("""
                \n=> ACCOUNT SETTINGS:
                ------------------------------------------------------
                1. Update Phone Number
                2. Change Password
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayAdminMainMenuNew() {
        System.out.println("""
                \n=> ADMIN CONTROL PANEL:
                ------------------------------------------------------
                1. Menu Management
                2. Order & Discount Management
                3. Customer & Delivery Partner Management
                4. Notification Management
                5. Account Settings
                0. LogOut""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayCustomerMainMenu() {
        System.out.println("""
                \n=> CUSTOMER MAIN MENU:
                ------------------------------------------------------
                1. Orders Management
                2. History & Notifications
                3. Account & Support
                0. Logout""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayOrdersManagementMenu() {
        System.out.println("""
                \n=> ORDERS MANAGEMENT:
                ------------------------------------------------------
                1. Add Item to Cart
                2. Remove Item from Cart
                3. Display Cart
                4. Place Order
                5. Display Menu
                6. Display Available Discounts
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayHistoryNotificationsMenu() {
        System.out.println("""
                \n=> HISTORY & NOTIFICATIONS:
                ------------------------------------------------------
                1. Show Order History
                2. Display Notifications
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayAccountSupportMenu() {
        System.out.println("""
                \n=> ACCOUNT & SUPPORT:
                ------------------------------------------------------
                1. Change Phone Number
                2. Change Password
                3. Customer Support
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }

    public static void displayUserFirstMenu() {
        System.out.println("""
                \n=> CUSTOMER MENU:
                ------------------------------------------------------
                1. Register New Customer
                2. Login
                0. Back""");
        System.out.println("\u2500".repeat(60));
    }
}


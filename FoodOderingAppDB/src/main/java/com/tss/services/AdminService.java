package com.tss.services;

import com.tss.exceptions.ItemNotFoundException;
import com.tss.exceptions.UserNotFoundException;
import com.tss.model.*;
import com.tss.factory.FoodItemFactory;
import com.tss.factory.UserFactory;
import com.tss.model.users.*;
import com.tss.repositories.*;
import com.tss.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AdminService {
    private Admin admin;
    private MenuService menuService;
    private UserService userService;
    private NotificationService notificationService;
    private MenuRepo menuRepo;
    private OrderRepo orderRepo;
    private OrderService orderService;
    private DeliveryPartnerRepo deliveryPartnerRepo;
    private DiscountRepo discountRepo;
    private CustomerRepo customerRepo;

    public AdminService(User admin) {
        this.admin = (Admin) admin;
        this.menuService = MenuService.getInstance();
        userService = UserService.getInstance();
        notificationService = NotificationService.getInstance();
        menuRepo=new MenuRepoImpl();
        orderRepo=new OrderRepoImpl();
        orderService=OrderService.getInstance();
        deliveryPartnerRepo=new DeliveryPartnerRepoImpl();
        discountRepo=new DiscountRepoImpl();
        customerRepo=new CustomerRepoImpl();
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void welcomeDisplay() {
        System.out.println("\nHii, " + admin.getName() + "\nWelcome Back!");
    }

    public void displayMenu() {
        menuService.displayMenu();
    }

    public void addCuisineType() {
        System.out.println("ADDING NEW CUISINE:");
        System.out.print("Add Cuisine Name: ");
        String cuisineName = Validate.validateCharAndNumberOnlyString();
        menuService.addNewCuisine(cuisineName);
        System.out.println(cuisineName + " Food Cuisine Type Successfully Added to Menu!");
    }

    public void addNewFoodItem() { // MENU REPO
        System.out.println("ADDING NEW FOOD ITEM:");
        List<CuisineType> cuisineList =menuService.getAllCuisines();

        if (cuisineList.isEmpty()) {
            System.out.println("No cuisine Available!");
            System.out.println("Add Cuisine First.");
            return;
        }
        System.out.println("Available Cuisine Type: ");

        int index = 0;
        for (CuisineType cuisineType : cuisineList) {
            System.out.println(index + ". " + cuisineType.getName());
            index++;
        }
        System.out.print("Choose Cuisine index from above: ");
        int i = Validate.validatePositiveInt();
        while (i >= cuisineList.size()) {
            System.out.print("Enter Valid index: ");
            i = Validate.validatePositiveInt();
        }

        System.out.print("Enter Food Item Name: ");
        String itemName = Validate.validateCharAndNumberOnlyString();

        System.out.print("Enter Price: ");
        double price = Validate.validatePositiveDouble();

        FoodItem newFoodItem = FoodItemFactory.getFoodItemInstance(itemName, price, cuisineList.get(i));

        menuService.addNewFoodItem(newFoodItem);
        System.out.println(itemName + " Added Successfully...");
    }

    public void changePrice() {
        if(menuService.isEmpty()){
            System.out.println("No Items Available!");
            return;
        }
        menuService.displayMenu();

        System.out.println("CHANGING PRICE: ");
        System.out.print("Enter Item-Id: ");
        long id = Validate.validatePositiveLong();

        FoodItem item = menuService.getItemFromId(id);

        if (item == null) {
            throw new ItemNotFoundException("No Such Item Exists!");
        }
        System.out.println("Current Price of " + item.getName() + " is " + item.getPrice());
        System.out.print("Enter New Price: ");
        double price = Validate.validatePositiveDouble();
        menuService.changePrice(id,price);
        System.out.println("Price Changed Successfully...");
    }


    public void printAllOrders() {
        System.out.println("ORDER HISTORY:");
        orderService.displayOrders(orderRepo.getAllOrders());
    }

    public void addNewDeliveryPartner() {
        System.out.println("ADDING NEW DELIVERY PARTNER:");
        User newDeliveryPartner = userService.makeUser(UserType.DELIVERY_PARTNER);
        deliveryPartnerRepo.addNewDeliveryPartner((DeliveryPartner) newDeliveryPartner);
        deliveryPartnerRepo.assignOrder();

        System.out.println("Delivery Partner " + newDeliveryPartner.getName() + " Added successfully...");
    }

    public void addDiscount() {
        System.out.println("ADDING DISCOUNT: ");
        System.out.print("Enter Minimum Amount: ");
        double minimumAmount = Validate.validatePositiveDouble();

        System.out.print("Enter Percentage (in 0. format): ");
        double discount = Validate.validatePositiveDouble();
        while (discount > 1) {
            System.out.print("Enter Below 1 (1 means 100%): ");
            discount = Validate.validatePositiveDouble();
        }
        if(discount==0){
            System.out.println("It's not applicable!");
            return;
        }
        discountRepo.addNewPriceDiscount(new PriceDiscount(minimumAmount,discount));
    }

    public void showDeliveryPartners() {
        List<DeliveryPartner> deliveryPartners=deliveryPartnerRepo.getAllDeliveryPartners();
        if (deliveryPartners.isEmpty()) {
            System.out.println("No Delivery Partner Found!");
            return;
        }
        System.out.println("DELIVERY PARTNERS:");
        for (DeliveryPartner deliveryPartner : deliveryPartners) {
            System.out.println(deliveryPartner);
        }
    }

    public void displayDiscounts() {
        System.out.println("AVAILABLE DISCOUNTS: ");
        List<Discount> discounts= discountRepo.getAllDiscounts();

        if (discounts.isEmpty()) {
            System.out.println("No Discounts Currently Available!");
            return;
        }
        for (Discount discount : discounts) {
            System.out.println(discount.getDescription());
        }
    }

    public void displayAllCustomers() {
        List<Customer> customers=customerRepo.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No Customer Found!");
            return;
        }
        System.out.println("CUSTOMERS:");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    public void sendNotificationToDeliveryPartners() {
        System.out.print("Write Notification Message: ");
        String message = Validate.validateNonEmptyString();

        System.out.print("Do you really want to Send this Notification?(y/n): ");
        boolean confirmation = Validate.validateYesNo();
        if (!confirmation) {
            return;
        }
        notificationService.broadcastDeliveryPartnerNotification(message, UserType.ADMIN);
        System.out.println("Notification sent...");
    }

    public void displayNotifications() {
        userService.displayUserNotifications(admin);
    }

    public void sendNotificationToCustomers() {
        System.out.print("Write Notification Message: ");
        String message = Validate.validateNonEmptyString();

        System.out.print("Do you really want to Send this Notification?(y/n): ");
        boolean confirmation = Validate.validateYesNo();
        if (!confirmation) {
            return;
        }
        notificationService.broadcastCustomerNotification(message, UserType.ADMIN);
        System.out.println("Notification sent...");
    }

    public void revenueDetails() {
        List<Order> orders=orderRepo.getAllOrders();
        if(orders.isEmpty()){
            System.out.println("Total Number Of Orders: " + 0);
            System.out.println("Total Earnings        : " + 0);
        }

        double totalRevenue = 0;

        for (Order myOrder : orders) {
            totalRevenue += myOrder.getFinalAmount();
        }
        int totalOrders = orders.size();

        System.out.println("Total Number Of Orders: " + totalOrders);
        System.out.println("Total Earnings        : " + totalRevenue);
    }

    public void removeItem() {
        System.out.println("REMOVING FOOD ITEM:");
        if (menuService.isEmpty()) {
            System.out.println("Menu is Empty!");
            return;
        }
        menuService.displayMenu();

        System.out.print("Enter Item-Id: ");
        long id = Validate.validatePositiveLong();
        if (menuService.removeItem(id)) {
            System.out.println("Removed successfully...");
            return;
        }
        System.out.println("Item Not Exists!");
    }

    public void removeCuisine() {
        System.out.println("REMOVING CUISINE:");
        List<CuisineType> cuisineList = menuService.getAllCuisines();
        if (cuisineList.isEmpty()) {
            System.out.println("No Cuisines Available!");
            return;
        }
        System.out.println("Available Cuisine Type with their IDs: ");
        for (CuisineType cuisineType : cuisineList) {
            System.out.println(cuisineType.getId() + ". " + cuisineType.getName());
        }
        System.out.print("Enter Id: ");
        long id = Validate.validatePositiveLong();

        if (menuService.removeCuisine(id)) {
            System.out.println("Removed successfully...");
            return;
        }
        System.out.println("Cuisine Not Exists!");
    }

    public void changePassword() {
        userService.changePassword(admin,UserType.ADMIN);
    }

    public void changePhoneNumber() {
        userService.changeNumber(admin, UserType.ADMIN);
    }

    public void displayPendingOrders() {
        orderService.displayOrders(orderRepo.getOrderFromStatus(OrderStatus.ACCEPTED));
    }

}

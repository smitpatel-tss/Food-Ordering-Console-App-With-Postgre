package com.tss.services;

import com.tss.exceptions.CartEmptyException;
import com.tss.exceptions.ItemNotFoundException;
import com.tss.exceptions.NoDeliveryPartnerAvailable;
import com.tss.exceptions.UserNotFoundException;
import com.tss.factory.UserFactory;
import com.tss.model.*;
import com.tss.model.users.Customer;
import com.tss.model.users.User;
import com.tss.model.users.UserType;
import com.tss.payments.PaymentMode;
import com.tss.repositories.*;
import com.tss.utils.Validate;

import java.util.List;


public class CustomerService {
    private Customer customer;
    private OrderService orderService;
    private MenuService menuService;
    private PaymentService paymentService;
    private InvoiceService invoiceService;
    private UserService userService;
    private NotificationService notificationService;
    private OrderRepo orderRepo;
    private DeliveryPartnerRepo deliveryPartnerRepo;
    private DiscountRepo discountRepo;
    private CustomerRepo customerRepo;
    private CartRepo cartRepo;

    public CustomerService(User customer) {
        this.customer = (Customer) customer;
        orderService = OrderService.getInstance();
        menuService = MenuService.getInstance();
        paymentService = new PaymentService();
        invoiceService = new InvoiceService();
        userService = UserService.getInstance();
        notificationService = NotificationService.getInstance();

        orderRepo=new OrderRepoImpl();
        orderService=OrderService.getInstance();
        deliveryPartnerRepo=new DeliveryPartnerRepoImpl();
        discountRepo=new DiscountRepoImpl();
        customerRepo=new CustomerRepoImpl();
        cartRepo=new CartRepoImpl();
    }

    public void welcomeDisplay() {
        System.out.println("\nHii, " + customer.getName() + "\nWelcome!");
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void showHistory() {
        System.out.println("HISTORY:");

        orderService.displayOrders(orderRepo.ordersFromCustomerId(customer.getId()));
    }

    public void addItemToCart() {
        if (menuService.isEmpty()) {
            System.out.println("Menu is Empty!");
            return;
        }
        menuService.displayMenu();
        System.out.print("Enter Item id: ");
        long id = Validate.validatePositiveLong();

        FoodItem item = menuService.getItemFromId(id);

        if (item == null) {
            throw new ItemNotFoundException("No Such Item Exists!");
        }
        System.out.print("Enter Quantity: ");
        int quantity = Validate.validatePositiveIntNonZero();
        cartRepo.addItemToCart(customer.getId(),item.getId(),quantity);
        System.out.println(quantity + " x " + item.getName() + " added to cart...");
    }

    public void removeItemFromCart() {
        if (cartRepo.isCartEmpty(customer.getId())) {
            throw new CartEmptyException();
        }
        System.out.print("Enter Item id: ");
        long id = Validate.validatePositiveLong();

        FoodItem item = menuService.getItemFromId(id);

        if (item == null) {
            System.out.println("No Item Found!");
            return;
        }

        Cart cart=cartRepo.getCart(customer.getId());

        if (!cart.getCart().containsKey(item)) {
            System.out.println("Item not in cart!");
            return;
        }
        System.out.print("Enter Quantity: ");
        int quantity = Validate.validatePositiveIntNonZero();
        while (quantity > cart.getCart().get(item)) {

            System.out.print("Enter Valid Quantity! " + cart.getCart().get(item) + " or Less: ");
            quantity = Validate.validatePositiveIntNonZero();
        }
        cartRepo.removeItemFromCart(customer.getId(),item.getId(),quantity);
        System.out.println(quantity + " x " + item.getName() + " removed from cart...");
    }

    public void displayCart() {

        if(cartRepo.isCartEmpty(customer.getId())){
            throw new CartEmptyException();
        }

        cartRepo.getCart(customer.getId()).displayCart();
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

    public void placeOrder() {

        displayCart();
        System.out.println("Do you want to place an Order?(y/n): ");
        if (!Validate.validateYesNo()) {
            System.out.println("Back to menu...");
            return;
        }

        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
            System.out.println("Enter Your Address:");
            String address = Validate.validateNonEmptyString();
            customer.setAddress(address);
            customerRepo.updateAddress(customer.getId(),address);
        }

        PaymentMode paymentMode = paymentService.choosePaymentMethod(cartRepo.calculateCartTotal(customer.getId()));
        if (paymentMode == null) {
            throw new IllegalArgumentException("Payment failed!");
        }
        Order myOrder=null;
        try{
            myOrder = orderService.placeOrder(paymentMode, customer.getId());
        }catch (CartEmptyException e){
            System.out.println(e.getMessage());
        }catch (NoDeliveryPartnerAvailable e){
            System.out.println(e.getMessage());
        }

        invoiceService.printInvoice(myOrder);
        System.out.println("Order Placed...");
    }

//    public void newCustomerRegister() {
//
//        User customer = userService.makeUser(UserType.CUSTOMER);
//
//
//        System.out.println("✔ New Customer "
//                + customer.getName()
//                + " Registered, with Phone Number "
//                + customer
//                .getAccountInfo().getPhoneNumber());
//    }


    public void displayMenu() {
        if (menuService.isEmpty()) {
            System.out.println("Menu is Empty!");
            return;
        }
        menuService.displayMenu();
    }

    public void displayNotifications() {
        userService.displayUserNotifications(customer);
    }

    public void changePassword() {
        userService.changePassword(customer,UserType.CUSTOMER);
    }

    public void changePhoneNumber() {
        userService.changeNumber(customer, UserType.CUSTOMER);
    }

    public void customerSupport() {
        System.out.println("CUSTOMER SUPPORT: ");
        System.out.print("Write Your Message: ");
        String message = Validate.validateNonEmptyString();
        System.out.print("Do you really want to send?(Y/N): ");
        if (!Validate.validateYesNo()) {
            return;
        }
        notificationService.sendNotification(message, UserType.CUSTOMER,UserType.ADMIN);
        System.out.println("Message Sent...");
    }

}

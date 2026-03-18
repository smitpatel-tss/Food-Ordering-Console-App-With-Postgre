package com.tss.services;

import com.tss.authentication.AccountInfo;
import com.tss.model.*;
import com.tss.model.users.Admin;
import com.tss.model.users.Customer;
import com.tss.model.users.DeliveryPartner;
import com.tss.payments.UPI;
import com.tss.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private MenuRepo menuRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private DeliveryPartnerRepo deliveryPartnerRepo;
    @Mock
    private DiscountRepo discountRepo;
    @Mock
    private CustomerRepo customerRepo;
    @Mock
    private MenuService menuService;
    @Mock
    private OrderService orderService;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;

    private Admin admin;
    private AdminService adminService;
    private CuisineType indianCuisine;
    private Cart sampleCart;

    @BeforeEach
    void setUp() {
        admin = new Admin(1L, "Rahul", 9000000001L, "rahul@123");
        indianCuisine = new CuisineType(1L, "Indian");

        HashMap<FoodItem, Integer> items = new HashMap<>();
        FoodItem biryani = new FoodItem(101L, "Paneer Biryani", 250.0, indianCuisine);
        items.put(biryani, 1);
        sampleCart = new Cart(items, 250.0);

        adminService = new AdminService(admin, menuRepo, orderRepo, deliveryPartnerRepo,
                discountRepo, customerRepo, menuService, orderService, userService, notificationService);
    }

    @Test
    void getAdminTest() {
        Admin result = adminService.getAdmin();
        assertNotNull(result);
        assertEquals("Rahul", result.getName());
    }

    @Test
    void setAdminTest() {
        Admin newAdmin = new Admin(2L, "Tanmay", 9000000002L, "tanmay@pass");
        adminService.setAdmin(newAdmin);
        assertEquals("Tanmay", adminService.getAdmin().getName());
    }

    @Test
    void printAllOrdersTest() {
        Order order = new Order(sampleCart, null, new UPI(250.0), 10L);
        when(orderRepo.getAllOrders()).thenReturn(Collections.singletonList(order));
        assertDoesNotThrow(() -> adminService.printAllOrders());
        verify(orderService, times(1)).displayOrders(anyList());
    }

    @Test
    void printAllOrdersEmptyTest() {
        when(orderRepo.getAllOrders()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> adminService.printAllOrders());
        verify(orderService, times(1)).displayOrders(Collections.emptyList());
    }

    @Test
    void revenueDetailsTest() {
        Order order1 = new Order(sampleCart, null, new UPI(250.0), 10L);
        Order order2 = new Order(sampleCart, null, new UPI(300.0), 11L);
        when(orderRepo.getAllOrders()).thenReturn(Arrays.asList(order1, order2));
        assertDoesNotThrow(() -> adminService.revenueDetails());
        verify(orderRepo, times(1)).getAllOrders();
    }

    @Test
    void revenueDetailsEmptyTest() {
        when(orderRepo.getAllOrders()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> adminService.revenueDetails());
    }

    @Test
    void showDeliveryPartnersTest() {
        DeliveryPartner dp = new DeliveryPartner(1L, "Divy", 9111111111L, "divy@pass", true, true);
        when(deliveryPartnerRepo.getAllDeliveryPartners()).thenReturn(Collections.singletonList(dp));
        assertDoesNotThrow(() -> adminService.showDeliveryPartners());
    }

    @Test
    void showDeliveryPartnersEmptyTest() {
        when(deliveryPartnerRepo.getAllDeliveryPartners()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> adminService.showDeliveryPartners());
    }

    @Test
    void displayAllCustomersTest() {
        Customer customer = new Customer(1L, "Amit", 9876543210L, "Home", "Street 12");
        when(customerRepo.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        assertDoesNotThrow(() -> adminService.displayAllCustomers());
    }

    @Test
    void displayAllCustomersEmptyTest() {
        when(customerRepo.getAllCustomers()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> adminService.displayAllCustomers());
    }

    @Test
    void displayDiscountsTest() {
        Discount discount = new PriceDiscount(200.0, 0.05);
        when(discountRepo.getAllDiscounts()).thenReturn(Collections.singletonList(discount));
        assertDoesNotThrow(() -> adminService.displayDiscounts());
        verify(discountRepo, times(1)).getAllDiscounts();
    }

    @Test
    void displayDiscountsEmptyTest() {
        when(discountRepo.getAllDiscounts()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> adminService.displayDiscounts());
    }

    @Test
    void displayPendingOrdersTest() {
        Order order = new Order(sampleCart, null, new UPI(250.0), 10L);
        when(orderRepo.getOrderFromStatus(OrderStatus.ACCEPTED))
                .thenReturn(Collections.singletonList(order));
        assertDoesNotThrow(() -> adminService.displayPendingOrders());
        verify(orderService, times(1)).displayOrders(anyList());
    }

    @Test
    void displayPendingOrdersEmptyTest() {
        when(orderRepo.getOrderFromStatus(OrderStatus.ACCEPTED))
                .thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> adminService.displayPendingOrders());
        verify(orderService, times(1)).displayOrders(Collections.emptyList());
    }
}
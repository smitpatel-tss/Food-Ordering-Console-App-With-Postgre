package com.tss.services;

import com.tss.authentication.AccountInfo;
import com.tss.exceptions.CartEmptyException;
import com.tss.model.*;
import com.tss.model.users.Customer;
import com.tss.payments.UPI;
import com.tss.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private OrderRepo orderRepo;
    @Mock
    private DeliveryPartnerRepo deliveryPartnerRepo;
    @Mock
    private DiscountRepo discountRepo;
    @Mock
    private CustomerRepo customerRepo;
    @Mock
    private CartRepo cartRepo;
    @Mock
    private OrderService orderService;
    @Mock
    private MenuService menuService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private InvoiceService invoiceService;

    private Customer customer;
    private CustomerService customerService;
    private FoodItem biryani;
    private Cart sampleCart;

    @BeforeEach
    void setUp() {
        AccountInfo accountInfo = new AccountInfo(9876543210L, "rahul123");
        customer = new Customer(1L, "Rahul", accountInfo);
        customer.setAddress("MG Road, Mumbai");

        CuisineType cuisine = new CuisineType(1L, "Indian");
        biryani = new FoodItem(101L, "Paneer Biryani", 250.0, cuisine);

        HashMap<FoodItem, Integer> items = new HashMap<>();
        items.put(biryani, 2);
        sampleCart = new Cart(items, 500.0);

        customerService = new CustomerService(customer,
                orderRepo, deliveryPartnerRepo, discountRepo,
                customerRepo, cartRepo, orderService, menuService,
                notificationService, userService, paymentService, invoiceService);
    }

    @Test
    void getCustomerTest() {
        Customer result = customerService.getCustomer();
        assertNotNull(result);
        assertEquals("Rahul", result.getName());
    }

    @Test
    void setCustomerTest() {
        Customer newCustomer = new Customer(2L, "Tanmay", new AccountInfo(9999999999L, "tanmay123"));
        customerService.setCustomer(newCustomer);
        assertEquals("Tanmay", customerService.getCustomer().getName());
    }

    @Test
    void showHistoryTest() {
        Order order = new Order(sampleCart, null, new UPI(500.0), 1L);
        when(orderRepo.ordersFromCustomerId(1L)).thenReturn(Collections.singletonList(order));
        assertDoesNotThrow(() -> customerService.showHistory());
        verify(orderService, times(1)).displayOrders(anyList());
    }

    @Test
    void showHistoryEmptyTest() {
        when(orderRepo.ordersFromCustomerId(1L)).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> customerService.showHistory());
        verify(orderService, times(1)).displayOrders(Collections.emptyList());
    }

    @Test
    void displayCartEmptyTest() {
        when(cartRepo.isCartEmpty(1L)).thenReturn(true);
        assertThrows(CartEmptyException.class, () -> customerService.displayCart());
    }

    @Test
    void displayCartTest() {
        when(cartRepo.isCartEmpty(1L)).thenReturn(false);
        when(cartRepo.getCart(1L)).thenReturn(sampleCart);
        assertDoesNotThrow(() -> customerService.displayCart());
    }

    @Test
    void displayDiscountsTest() {
        Discount discount = new PriceDiscount(300.0, 0.10);
        when(discountRepo.getAllDiscounts()).thenReturn(Collections.singletonList(discount));
        assertDoesNotThrow(() -> customerService.displayDiscounts());
        verify(discountRepo, times(1)).getAllDiscounts();
    }

    @Test
    void displayDiscountsEmptyTest() {
        when(discountRepo.getAllDiscounts()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> customerService.displayDiscounts());
    }

    @Test
    void displayMenuEmptyTest() {
        when(menuService.isEmpty()).thenReturn(true);
        assertDoesNotThrow(() -> customerService.displayMenu());
        verify(menuService, never()).displayMenu();
    }

    @Test
    void displayMenuTest() {
        when(menuService.isEmpty()).thenReturn(false);
        assertDoesNotThrow(() -> customerService.displayMenu());
        verify(menuService, times(1)).displayMenu();
    }
}
package com.tss.services;

import com.tss.exceptions.CartEmptyException;
import com.tss.exceptions.NoDeliveryPartnerAvailable;
import com.tss.model.*;
import com.tss.model.users.UserType;
import com.tss.payments.PaymentMode;
import com.tss.payments.UPI;
import com.tss.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private DeliveryPartnerRepo deliveryPartnerRepo;
    @Mock
    private CartRepo cartRepo;
    @Mock
    private DiscountRepo discountRepo;

    @InjectMocks
    private OrderService orderService;

    private final long CUSTOMER_ID = 1L;
    private PaymentMode paymentMode;
    private Cart cart;
    private CuisineType cuisine;
    private FoodItem foodItem;

    @BeforeEach
    void setUp() {
        cuisine = new CuisineType(1L, "Indian");
        foodItem = new FoodItem(101L, "Paneer Biryani", 250.0, cuisine);
        HashMap<FoodItem, Integer> items = new HashMap<>();
        items.put(foodItem, 2);
        cart = new Cart(items, 500.0);
        paymentMode = new UPI(500.0);
    }

    @Test
    void placeOrderTest() {
        when(cartRepo.isCartEmpty(CUSTOMER_ID)).thenReturn(false);
        when(deliveryPartnerRepo.isDeliveryPartnersEmpty()).thenReturn(false);
        when(cartRepo.getCart(CUSTOMER_ID)).thenReturn(cart);
        when(discountRepo.giveMaxPossibleDiscount(500.0)).thenReturn(null);
        doNothing().when(orderRepo).placeNewOrder(any(Order.class));
        doNothing().when(notificationService).sendNotification(anyLong(), anyString(), any(UserType.class), any(UserType.class));
        doNothing().when(deliveryPartnerRepo).assignOrder();
        doNothing().when(cartRepo).clearCart(CUSTOMER_ID);

        Order result = orderService.placeOrder(paymentMode, CUSTOMER_ID);

        assertNotNull(result);
        assertEquals(CUSTOMER_ID, result.getCustomerId());
        assertEquals(OrderStatus.ACCEPTED, result.getStatus());
        assertEquals(500.0, result.getFinalAmount());
        verify(orderRepo, times(1)).placeNewOrder(any(Order.class));
        verify(cartRepo, times(1)).clearCart(CUSTOMER_ID);
    }

    @Test
    void placeOrderCartEmptyTest() {
        when(cartRepo.isCartEmpty(CUSTOMER_ID)).thenReturn(true);
        assertThrows(CartEmptyException.class, () -> orderService.placeOrder(paymentMode, CUSTOMER_ID));
        verify(orderRepo, never()).placeNewOrder(any());
    }

    @Test
    void placeOrderNoPartnerTest() {
        when(cartRepo.isCartEmpty(CUSTOMER_ID)).thenReturn(false);
        when(deliveryPartnerRepo.isDeliveryPartnersEmpty()).thenReturn(true);
        assertThrows(NoDeliveryPartnerAvailable.class, () -> orderService.placeOrder(paymentMode, CUSTOMER_ID));
        verify(orderRepo, never()).placeNewOrder(any());
    }

    @Test
    void placeOrderWithDiscountTest() {
        Discount discount = new PriceDiscount(400.0, 0.10);
        when(cartRepo.isCartEmpty(CUSTOMER_ID)).thenReturn(false);
        when(deliveryPartnerRepo.isDeliveryPartnersEmpty()).thenReturn(false);
        when(cartRepo.getCart(CUSTOMER_ID)).thenReturn(cart);
        when(discountRepo.giveMaxPossibleDiscount(500.0)).thenReturn(discount);
        doNothing().when(orderRepo).placeNewOrder(any(Order.class));
        doNothing().when(notificationService).sendNotification(anyLong(), anyString(), any(UserType.class), any(UserType.class));
        doNothing().when(deliveryPartnerRepo).assignOrder();
        doNothing().when(cartRepo).clearCart(CUSTOMER_ID);

        Order result = orderService.placeOrder(paymentMode, CUSTOMER_ID);

        assertNotNull(result);
        assertEquals(450.0, result.getFinalAmount(), 0.01);
    }

    @Test
    void displayOrdersTest() {
        Order order = new Order(cart, null, paymentMode, CUSTOMER_ID);
        List<Order> orders = Collections.singletonList(order);
        assertDoesNotThrow(() -> orderService.displayOrders(orders));
    }

    @Test
    void displayOrdersEmptyTest() {
        List<Order> emptyOrders = Collections.emptyList();
        assertDoesNotThrow(() -> orderService.displayOrders(emptyOrders));
    }
}
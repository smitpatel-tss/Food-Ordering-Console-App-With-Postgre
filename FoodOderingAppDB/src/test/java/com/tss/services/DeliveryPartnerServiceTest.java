package com.tss.services;

import com.tss.authentication.AccountInfo;
import com.tss.model.*;
import com.tss.model.users.DeliveryPartner;
import com.tss.payments.UPI;
import com.tss.repositories.DeliveryPartnerRepo;
import com.tss.repositories.OrderRepo;
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
public class DeliveryPartnerServiceTest {

    @Mock
    private DeliveryPartnerRepo deliveryPartnerRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private OrderService orderService;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;

    private DeliveryPartner deliveryPartner;
    private DeliveryPartnerService deliveryPartnerService;
    private Cart sampleCart;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        AccountInfo accountInfo = new AccountInfo(9111111111L, "divy123");
        deliveryPartner = new DeliveryPartner(1L, "Divy", accountInfo);

        CuisineType cuisine = new CuisineType(1L, "Indian");
        FoodItem item = new FoodItem(101L, "Paneer Biryani", 250.0, cuisine);
        HashMap<FoodItem, Integer> items = new HashMap<>();
        items.put(item, 1);
        sampleCart = new Cart(items, 250.0);

        sampleOrder = new Order(sampleCart, null, new UPI(250.0), 10L);

        deliveryPartnerService = new DeliveryPartnerService(deliveryPartner,
                deliveryPartnerRepo, orderRepo, orderService, userService, notificationService);
    }

    @Test
    void getDeliveryPartnerTest() {
        DeliveryPartner result = deliveryPartnerService.getDeliveryPartner();
        assertNotNull(result);
        assertEquals("Divy", result.getName());
    }

    @Test
    void setDeliveryPartnerTest() {
        DeliveryPartner newPartner = new DeliveryPartner(2L, "Tanmay",
                new AccountInfo(9222222222L, "tanmay123"));
        deliveryPartnerService.setDeliveryPartner(newPartner);
        assertEquals("Tanmay", deliveryPartnerService.getDeliveryPartner().getName());
    }

    @Test
    void printOrdersHistoryTest() {
        when(orderRepo.ordersFromDeliveryAgentId(1L))
                .thenReturn(Collections.singletonList(sampleOrder));
        assertDoesNotThrow(() -> deliveryPartnerService.printOrdersHistory());
        verify(orderService, times(1)).displayOrders(anyList());
    }

    @Test
    void printOrdersHistoryEmptyTest() {
        when(orderRepo.ordersFromDeliveryAgentId(1L))
                .thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> deliveryPartnerService.printOrdersHistory());
        verify(orderService, times(1)).displayOrders(Collections.emptyList());
    }
}
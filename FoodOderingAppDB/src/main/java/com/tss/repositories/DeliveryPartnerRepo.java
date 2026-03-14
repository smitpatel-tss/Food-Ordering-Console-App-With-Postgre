package com.tss.repositories;

import com.tss.model.Order;
import com.tss.model.users.DeliveryPartner;

import java.util.List;

public interface DeliveryPartnerRepo {
    List<DeliveryPartner> getAllDeliveryPartners();
    void addNewDeliveryPartner(DeliveryPartner deliveryPartner);
    List<Order> pendingOrders(long deliveryPartnerId);
}

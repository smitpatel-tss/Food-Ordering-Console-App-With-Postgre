package com.tss.factory;

import com.tss.authentication.AccountInfo;
import com.tss.model.users.*;

import java.util.Random;

public class UserFactory {
    static Random random=new Random();

    public User getUser(String name, UserType type, AccountInfo info){
        if(type==UserType.ADMIN){
            return new Admin(name,info);
        }
        if(type==UserType.DELIVERY_PARTNER){
            return new DeliveryPartner(name,info);
        }
        if(type==UserType.CUSTOMER){
            return new Customer(name,info);
        }
        return null;
    }

}

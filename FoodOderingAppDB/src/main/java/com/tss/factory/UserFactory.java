package com.tss.factory;

import com.tss.authentication.AccountInfo;
import com.tss.model.users.*;
import com.tss.repositories.UserRepository;

import java.util.Random;

public class UserFactory {
    static Random random=new Random();

    public User getUser(String name, UserType type, AccountInfo info){
        if(type==UserType.ADMIN){
            return new Admin(getUniqueId(),name,info);
        }
        if(type==UserType.DELIVERY_PARTNER){
            return new DeliveryPartner(getUniqueId(),name,info);
        }
        if(type==UserType.CUSTOMER){
            return new Customer(getUniqueId(),name,info);
        }
        return null;
    }

    public long getUniqueId(){
        UserRepository userRepo=UserRepository.getInstance();
        long newNumber;
        boolean flag;
        do {
            newNumber = random.nextLong(1_000_000_000L, 10_000_000_000L);
            flag = false;

            for (User user:userRepo.getUsers()) {
                if (user.getId() == newNumber) {
                    flag = true;
                    break;
                }
            }
        } while (flag);

        return newNumber;
    }
}

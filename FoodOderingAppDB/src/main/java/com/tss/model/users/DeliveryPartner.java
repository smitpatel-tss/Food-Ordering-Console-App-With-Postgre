package com.tss.model.users;

import com.tss.authentication.AccountInfo;

public class DeliveryPartner implements User{
    private long id;
    private String name;
    private AccountInfo accountInfo;
    private UserType userType;
    private boolean is_active;
    private boolean is_available;

    public DeliveryPartner(long id, String name, AccountInfo accountInfo) {
        this.id = id;
        this.name = name;
        this.accountInfo=accountInfo;
        this.is_active=true;
        this.is_available=true;
        this.userType=UserType.DELIVERY_PARTNER;
    }

    public DeliveryPartner(String name, long phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.accountInfo=new AccountInfo(phoneNumber,password);
        this.is_active=true;
        this.is_available=true;
        this.userType=UserType.DELIVERY_PARTNER;
    }

    public DeliveryPartner(long id, String name, long phoneNumber,boolean is_active,boolean is_available) {
        this.id = id;
        this.name = name;
        this.accountInfo=new AccountInfo(phoneNumber);
        this.is_active=is_active;
        this.is_available=is_available;
        this.userType=UserType.DELIVERY_PARTNER;
    }

    public DeliveryPartner(long id, String name, long phoneNumber,String password,boolean is_active,boolean is_available) {
        this.id = id;
        this.name = name;
        this.accountInfo=new AccountInfo(phoneNumber,password);
        this.is_active=is_active;
        this.is_available=is_available;
        this.userType=UserType.DELIVERY_PARTNER;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountInfo getAccountInfo(){
        return accountInfo;
    }

    public UserType getUserType(){
        return userType;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Phone: %s",
                id,
                name,
                accountInfo.getPhoneNumber()
        );
    }
}

package com.tss.model.users;

import com.tss.authentication.AccountInfo;

public class Customer implements User{
    private long id;
    private String name;
    private AccountInfo accountInfo;
    private String address;
    private UserType userType;

    public Customer(long id, String name, AccountInfo accountInfo) {
        this.id = id;
        this.name = name;
        this.accountInfo=accountInfo;
        this.userType=UserType.CUSTOMER;
    }

    public Customer(String name, AccountInfo accountInfo) {
        this.name = name;
        this.accountInfo=accountInfo;
        this.userType=UserType.CUSTOMER;
    }

    public Customer(long id, String name, long phone,String address) {
        this.id = id;
        this.name = name;
        this.accountInfo=new AccountInfo(phone);
        this.address=address;
        this.userType=UserType.CUSTOMER;
    }

    public Customer(long id, String name, long phone,String password,String address) {
        this.id = id;
        this.name = name;
        this.accountInfo=new AccountInfo(phone,password);
        this.address=address;
        this.userType=UserType.CUSTOMER;
    }

    public AccountInfo getAccountInfo(){
        return accountInfo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
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

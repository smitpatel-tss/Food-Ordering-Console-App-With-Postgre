package com.tss.model.users;

import com.tss.authentication.AccountInfo;

public class Admin implements User{
    private long id;
    private String name;
    private AccountInfo accountInfo;
    private UserType userType;

    public Admin(long id, String name, AccountInfo accountInfo) {
        this.id = id;
        this.name = name;
        this.accountInfo=accountInfo;
        this.userType=UserType.ADMIN;
    }

    public Admin(long id, String name, long phone,String password) {
        this.id = id;
        this.name = name;
        this.accountInfo=new AccountInfo(phone,password);
        this.userType=UserType.ADMIN;
    }

    public AccountInfo getAccountInfo(){
        return accountInfo;
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
}

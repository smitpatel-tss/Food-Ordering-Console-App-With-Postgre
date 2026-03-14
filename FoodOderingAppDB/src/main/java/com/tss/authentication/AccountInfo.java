package com.tss.authentication;

public class AccountInfo {
    private long phoneNumber;
    private String password;

    public AccountInfo(long phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public AccountInfo(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setPhoneNumber(long newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }
}

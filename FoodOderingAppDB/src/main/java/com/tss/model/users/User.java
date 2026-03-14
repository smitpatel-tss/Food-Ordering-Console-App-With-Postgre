package com.tss.model.users;

import com.tss.authentication.AccountInfo;

public interface User {
    long getId();
    String getName();
    AccountInfo getAccountInfo();
    UserType getUserType();
}

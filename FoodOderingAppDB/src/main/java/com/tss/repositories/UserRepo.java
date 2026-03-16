package com.tss.repositories;

import com.tss.model.users.Admin;
import com.tss.model.users.User;
import com.tss.model.users.UserType;

public interface UserRepo {
    boolean canAddNumber(long number, UserType type);
    boolean checkPassword(long number, String password, UserType type);
    User getUserFromId(long id);
    User getUserFromNumber(long phone, UserType type);
    boolean changePassword(long phone, String newPassword, UserType type);
    boolean changePhoneNumber(long userId, long newPhone, UserType type);
    void addAdmin(Admin admin);
}

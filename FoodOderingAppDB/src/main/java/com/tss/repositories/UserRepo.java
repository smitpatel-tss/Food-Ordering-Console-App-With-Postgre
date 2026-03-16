package com.tss.repositories;

import com.tss.model.users.User;
import com.tss.model.users.UserType;

public interface UserRepo {
    boolean canAddNumber(long number, UserType type);
    boolean checkPassword(long number, String password, UserType type);
    User getUserFromId(long id);
    User getUserFromNumber(long phone, UserType type);
    boolean changePassword(long phone, String newPassword, UserType type);
}

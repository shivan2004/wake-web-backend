package com.shivan.wakeWeb.wakeWeb.services;

import com.shivan.wakeWeb.wakeWeb.entities.User;

public interface UserService {
    User getCurrentUser();
    Long getUsersCount();

    User getUserById(Long userId);
}

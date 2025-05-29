package com.shivan.wakeWeb.wakeWeb.services;

import com.shivan.wakeWeb.wakeWeb.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(String email, String fullName);

    //todo;remove later
    LoginResponseDTO login(String email);
}

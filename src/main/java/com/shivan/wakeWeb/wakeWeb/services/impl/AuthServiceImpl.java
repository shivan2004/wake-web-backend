package com.shivan.wakeWeb.wakeWeb.services.impl;

import com.shivan.wakeWeb.wakeWeb.dto.LoginResponseDTO;
import com.shivan.wakeWeb.wakeWeb.entities.User;
import com.shivan.wakeWeb.wakeWeb.repositories.UserRepository;
import com.shivan.wakeWeb.wakeWeb.security.JWTService;
import com.shivan.wakeWeb.wakeWeb.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Override
    public LoginResponseDTO login(String email, String fullName) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            user = User.builder()
                    .email(email)
                    .fullName(fullName)
                    .urlsList(new ArrayList<>())
                    .build();

            userRepository.save(user);
        }
        return sendLoginResponse(user);
    }


    //todo;remove later
    @Override
    public LoginResponseDTO login(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) return sendLoginResponse(user);
        user = User.builder()
                .email(email)
                .urlsList(new ArrayList<>())
                .build();

        userRepository.save(user);
        return sendLoginResponse(user);
    }

    private LoginResponseDTO sendLoginResponse(User user) {
        return new LoginResponseDTO(jwtService.generateAccessToken(user));

    }
}

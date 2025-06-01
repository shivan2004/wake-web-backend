package com.shivan.wakeWeb.wakeWeb.services.impl;

import com.shivan.wakeWeb.wakeWeb.entities.User;
import com.shivan.wakeWeb.wakeWeb.exceptions.ResourceNotFoundException;
import com.shivan.wakeWeb.wakeWeb.repositories.UserRepository;
import com.shivan.wakeWeb.wakeWeb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!isUserExists(user)) throw new ResourceNotFoundException("Illegal access, JWT token of another user");
        return user;
    }

    @Override
    public Long getUsersCount() {
        return userRepository.count();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email : " + email));
    }


    private boolean isUserExists(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
}

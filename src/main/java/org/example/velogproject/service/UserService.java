package org.example.velogproject.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.velogproject.domain.User;
import org.example.velogproject.exception.DuplicateEmailException;
import org.example.velogproject.exception.DuplicateUsernameException;
import org.example.velogproject.model.UserDto;
import org.example.velogproject.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        logger.debug("Attempting login for username: {}", username);
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            logger.debug("Login successful for username: {}", username);
            return user; // 로그인 성공 시 User 객체 반환
        }
        logger.debug("Login failed for username: {}", username);
        return null;
    }

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); //비밀번호 인코딩
        user.setName(userDto.getName());
        return save(user);
    }

    public User save(User user) {
        logger.debug("Saving user: {}", user.getUsername());
        if (findByUsername(user.getUsername()) != null) {
            logger.error("Duplicate username: {}", user.getUsername());
            throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
        }
        if (findByEmail(user.getEmail()) != null) {
            logger.error("Duplicate email: {}", user.getEmail());
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean isUsernameExists(String username) {
        return findByUsername(username) != null;
    }

    public boolean isEmailExists(String email) {
        return findByEmail(email) != null;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.debug("No authentication found or user is not authenticated");
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else if (principal instanceof String) {
            String username = (String) principal;
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}

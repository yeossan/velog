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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true); // true = create if not exists
            session.setAttribute("user", user);
            return user;
        }
        return null;
    }

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        return save(user);
    }

    public User save(User user) {
        if (findByUsername(user.getUsername()) != null) {
            throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
        }
        if (findByEmail(user.getEmail()) != null) {
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

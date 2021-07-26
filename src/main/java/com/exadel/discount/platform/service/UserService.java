package com.exadel.discount.platform.service;

import com.exadel.discount.platform.config.JWTUtil;
import com.exadel.discount.platform.domain.JWTResponse;
import com.exadel.discount.platform.domain.User;
import com.exadel.discount.platform.mapper.UserMapper;
import com.exadel.discount.platform.model.dto.UserDtoResponse;
import com.exadel.discount.platform.repository.UserRepository;
import com.exadel.discount.platform.domain.MyUserDetails;
import com.exadel.discount.platform.model.dto.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;

    public List<UserDtoResponse> getUsers() {
        return userMapper.map(userRepository.findAll());
    }

    public JWTResponse login(UserLoginDTO userLogin){
        UserDetails userDetails = loadUserByUsername(userLogin.getEmail());
        return new JWTResponse("Bearer " + jwtUtil.generateTOKEN(userDetails));
    }

    public String loginGenerateRefreshToken(UserLoginDTO userLogin){
        UserDetails userDetails = loadUserByUsername(userLogin.getEmail());
        return jwtUtil.generateRefreshToken(userDetails);
    }

    public JWTResponse generateAccessToken(String token) {
        UserDetails userDetails = loadUserByUsername(jwtUtil.extractUsernameRefreshToken(token));
        return new JWTResponse("Bearer " + jwtUtil.generateTOKEN(userDetails));
    }

    public void authenticate(UserLoginDTO userLogin) throws BadCredentialsException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findUserByEmail(s);
        if (user == null){
            throw new UsernameNotFoundException(s);
        }
        return new MyUserDetails(user);
    }
}

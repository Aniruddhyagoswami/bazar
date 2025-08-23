package org.ecommerce.project.controller;

import jakarta.validation.Valid;
import org.ecommerce.project.model.AppRole;
import org.ecommerce.project.model.Role;
import org.ecommerce.project.model.User;
import org.ecommerce.project.repository.UserRepository;
import org.ecommerce.project.security.jwt.JwtUtils;
import org.ecommerce.project.security.request.LoginRequest;
import org.ecommerce.project.security.request.SignupRequest;
import org.ecommerce.project.security.response.MessageResponse;
import org.ecommerce.project.security.response.UserInfoResponse;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@RestController
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try{
            authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        }catch (AuthenticationException exception){
            Map<String,Object> map=new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status","false");
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        String JwtToken=jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        UserInfoResponse userInfoResponse =new UserInfoResponse(userDetails.getId(),
                JwtToken,
                userDetails.getUsername(),
                roles);
        return new ResponseEntity<Object>(userInfoResponse,HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        } else if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        User user=new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );
        Set<String> strRoles=signupRequest.getRoles();
        Set<Role> roles=new HashSet<>();
        if (strRoles==null){
            Role userRole=roleRepository.findBYRoleName(AppRole.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        break;
                        case "seller":
                        break;
                        case "admin":
                        break;

                }
            });
        }
    }


}

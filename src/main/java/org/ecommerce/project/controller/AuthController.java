package org.ecommerce.project.controller;

import jakarta.validation.Valid;
import org.ecommerce.project.model.AppRole;
import org.ecommerce.project.model.Role;
import org.ecommerce.project.model.User;
import org.ecommerce.project.repository.RoleRepository;
import org.ecommerce.project.repository.UserRepository;
import org.ecommerce.project.security.jwt.JwtUtils;
import org.ecommerce.project.security.request.LoginRequest;
import org.ecommerce.project.security.request.SignupRequest;
import org.ecommerce.project.security.response.MessageResponse;
import org.ecommerce.project.security.response.UserInfoResponse;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
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
        ResponseCookie jwtCookie=jwtUtils.generateJwtCookie(userDetails);
        List<String> roles=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        UserInfoResponse userInfoResponse =new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),
                roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(userInfoResponse);
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
            Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                        case "admin":
                            Role adminRole=roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                            roles.add(adminRole);
                        break;
                        case "seller":
                            Role sellerRole=roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                            roles.add(sellerRole);
                        break;

                    default:
                        Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null){
            return authentication.getName();
        }else {
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );

        return ResponseEntity.ok().body(userInfoResponse);
    }


    @PostMapping("/signout")
    public ResponseEntity<?> signout(Authentication authentication){
        if (authentication != null){
            ResponseCookie cookie=jwtUtils.getCleanJwtCookie();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Successfully signed out"));
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
    }

}

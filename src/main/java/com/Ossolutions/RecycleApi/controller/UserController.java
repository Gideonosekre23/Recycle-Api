package com.Ossolutions.RecycleApi.controller;

import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Model.UserLeaderboardInfo;
import com.Ossolutions.RecycleApi.Service.UserNotFoundException;
import com.Ossolutions.RecycleApi.Service.UserService;
import com.Ossolutions.RecycleApi.security.JwtResponse;
import com.Ossolutions.RecycleApi.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            authenticate(user.getUsername(), user.getPassword());

            final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            final String token = jwtUtil.generateToken(userDetails.getUsername());

            // Fetch additional user details
            User loggedInUser = userService.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Create a response containing token and user details
            JwtResponse jwtResponse = new JwtResponse(token, loggedInUser.getUsername(), loggedInUser.getFullName(), loggedInUser.getPoints());

            return ResponseEntity.ok(jwtResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam String username) {
        // Check if the user is an admin
        if (userService.isAdmin(username)) {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } else {
            // Return an error response if the user is not an admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{username}")
    public Optional<User> getUsersByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<UserLeaderboardInfo>> getUsersByLocation(@PathVariable String location, @RequestParam String username) {
        List<UserLeaderboardInfo> users = userService.getUsersByLocation(location, username);
        if (users == null) {
            logger.error("Error fetching users by location");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> initiatePasswordReset(@RequestBody Map<String, String> resetRequest) {
        String username = resetRequest.get("username");
        String email = resetRequest.get("email");

        // Perform validation and initiate password reset
        userService.generatePasswordResetToken(username, email);

        return ResponseEntity.ok("Password reset initiated. Check your email for instructions.");
    }


    @PostMapping("/reset-password-confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestBody Map<String, String> resetRequest) {
        String token = resetRequest.get("token");
        String newPassword = resetRequest.get("newPassword");

        // Perform validation and complete password reset
        boolean isResetSuccessful = userService.resetPasswordWithToken(token, newPassword);

        if (isResetSuccessful) {
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password");
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserLeaderboardInfo>> getLeaderboard(
            @RequestBody Map<String, String> requestBody
    ) {
        try {
            String username = requestBody.get("username");
            String location = requestBody.get("location");

            List<UserLeaderboardInfo> leaderboard = userService.getLeaderboard(location, username);

            if (leaderboard == null) {
                logger.error("Leaderboard is null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            // Exclude "admin" from the leaderboard
            leaderboard = leaderboard.stream()
                    .filter(user -> !user.getUsername().equals("admin"))
                    .collect(Collectors.toList());

            // Set the position for every user
            for (int i = 0; i < leaderboard.size(); i++) {
                leaderboard.get(i).setPosition(i + 1);
            }

            logger.info("Leaderboard size: " + leaderboard.size());
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            logger.error("Error in getLeaderboard: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}

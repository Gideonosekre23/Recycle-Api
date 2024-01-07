package com.Ossolutions.RecycleApi.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.Ossolutions.RecycleApi.Model.PasswordResetToken;
import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Model.UserLeaderboardInfo;
import com.Ossolutions.RecycleApi.Repository.PasswordResetTokenRepository;
import com.Ossolutions.RecycleApi.Repository.UserRepository;
import com.Ossolutions.RecycleApi.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private JavaMailSender javaMailSender;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }


    @Override
    public User registerUser(User user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // If old password is not provided, allow the change (for reset scenarios)
                if (oldPassword == null || passwordEncoder.matches(oldPassword, user.getPassword())) {
                    // Encrypt the new password before updating
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("Error updating password: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserLeaderboardInfo> getUsersByLocation(String location, String requestingUsername) {
        // Check if the requesting user is an admin
        if (!isAdmin(requestingUsername)) {
            return null; // Or throw an exception indicating insufficient permissions
        }

        List<User> users;

        if (location != null) {
            // Fetch users from the specified location
            users = userRepository.findByLocation(location);
        } else {
            // Fetch all users if location is not specified
            users = userRepository.findAll();
        }

        // Create a list of UserLeaderboardInfo objects with restricted information
        List<UserLeaderboardInfo> result = new ArrayList<>();
        for (User user : users) {
            UserLeaderboardInfo userInfo = new UserLeaderboardInfo(
                    user.getUsername(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getPoints(),
                    0
            );
            result.add(userInfo);
        }

        return result;
    }


    public void updateUser(User user) {
        userRepository.save(user);
    }



    @Override
    public boolean isAdmin(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(user -> "admin".equals(user.getUserType())).orElse(false);
    }

    @Override
    public List<UserLeaderboardInfo> getLeaderboard(String location, String username) {
        List<User> leaderboard;
        if (location == null || location.isEmpty()) {
            // If location is not provided, retrieve overall leaderboard based on points
            leaderboard = userRepository.findByOrderByPointsDesc();
        } else {
            // If location is provided, retrieve leaderboard for that specific location
            leaderboard = userRepository.findByLocationOrderByPointsDesc(location);
        }

        if (leaderboard == null) {
            logger.error("Leaderboard is null");
            return null;
        }

        // Convert User objects to UserLeaderboardInfo
        List<UserLeaderboardInfo> leaderboardInfoList = leaderboard.stream()
                .map(user -> new UserLeaderboardInfo(
                        user.getUsername(),
                        user.getFullName(),
                        user.getPoints(),
                        0 // You can set position to 0 or any default value for getUsersByLocation
                ))
                .collect(Collectors.toList());

        // Find the index of the searched user in the sorted leaderboard
        int userIndex = IntStream.range(0, leaderboardInfoList.size())
                .filter(i -> leaderboardInfoList.get(i).getUsername().equals(username))
                .findFirst()
                .orElse(-1);

        // If the searched user is not found, return null or handle accordingly
        if (userIndex == -1) {
            logger.error("Searched user not found in leaderboard");
            return null;
        }

        // Set the correct position for the searched user
        leaderboardInfoList.get(userIndex).setPosition(userIndex + 1);

        return leaderboardInfoList;
    }




    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return UserDetailsImpl.build(user);
    }


    @Override
    public void generatePasswordResetToken(String username, String email) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Perform additional validation, e.g., check if the provided email matches the user's email

        String resetToken = UUID.randomUUID().toString();

        // Save the reset token in the separate table
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDateTime(LocalDateTime.now().plusHours(1)); // Set expiration to 1 hour
        passwordResetTokenRepository.save(passwordResetToken);

        // Send resetToken to the user via email
        sendPasswordResetEmail(user.getEmail(), resetToken);
    }
    private void sendPasswordResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n\n"
                + "http://your-app-url/reset-password?token=" + resetToken);

        javaMailSender.send(message);
    }
    @Override
    public boolean resetPasswordWithToken(String resetToken, String newPassword) {
        // Find the user based on the reset token
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository.findByToken(resetToken);

        if (passwordResetTokenOptional.isPresent()) {
            PasswordResetToken passwordResetToken = passwordResetTokenOptional.get();

            if (passwordResetToken.getExpirationDateTime().isAfter(LocalDateTime.now())) {
                User user = passwordResetToken.getUser();

                // Reset the password
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                // Remove the used token from the table
                passwordResetTokenRepository.delete(passwordResetToken);

                return true;
            }
        }

        return false;
    }
    @Override
    public boolean isResetTokenValid(String resetToken) {
        // Check if the reset token is present in the separate table and is not expired
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository.findByToken(resetToken);

        return passwordResetTokenOptional.isPresent() &&
                passwordResetTokenOptional.get().getExpirationDateTime().isAfter(LocalDateTime.now());
    }
}

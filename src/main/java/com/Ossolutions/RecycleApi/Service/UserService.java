package com.Ossolutions.RecycleApi.Service;

import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Model.UserLeaderboardInfo;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    User loginUser(String username, String password);

    List<User> getAllUsers();
    boolean updatePassword(String username, String oldPassword, String newPassword);


    Optional<User> findByUsername(String username);

    List<UserLeaderboardInfo> getUsersByLocation(String location, String requestingUsername);



    List<UserLeaderboardInfo> getLeaderboard(String location, String username);

    boolean isAdmin(String username);

    UserDetails loadUserByUsername(String username);

    void generatePasswordResetToken(String username,String email);



    boolean isResetTokenValid(String resetToken);


    void updateUser(User user);

    boolean resetPasswordWithToken(String token, String newPassword);
}

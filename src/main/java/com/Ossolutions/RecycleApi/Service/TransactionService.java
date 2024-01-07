package com.Ossolutions.RecycleApi.Service;

import com.Ossolutions.RecycleApi.Model.Material;
import com.Ossolutions.RecycleApi.Model.RecyclingCenter;
import com.Ossolutions.RecycleApi.Model.Transaction;
import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Repository.MaterialRepository;
import com.Ossolutions.RecycleApi.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Ossolutions.RecycleApi.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RecyclingCenterService recyclingCenterService;
    private final UserService userService;
    private  MaterialRepository materialRepository;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            RecyclingCenterService recyclingCenterService,
            UserService userService,
            MaterialRepository materialRepository) {
        this.transactionRepository = transactionRepository;
        this.recyclingCenterService = recyclingCenterService;
        this.userService = userService;
        this.materialRepository = materialRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction makeTransaction(String username, String centerName, List<String> materialNames) {
        // Fetch user and recycling center by name
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        RecyclingCenter recyclingCenter = recyclingCenterService.getRecyclingCenterByName(centerName)
                .orElseThrow(() -> new RuntimeException("Recycling center not found with name: " + centerName));


        List<Material> materials = fetchMaterialsByName(materialNames);

        // Calculate totalPoints based on selected materials and their prices
        int totalPoints = calculateTotalPoints(materials);

        // Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setRecyclingCenter(recyclingCenter);
        transaction.setMaterials(materials);
        transaction.setTotalPoints(totalPoints);
        transaction.setTransactionDateTime(LocalDateTime.now());

        // Save the transaction
        transactionRepository.save(transaction);

        // Update user's total points
        user.setPoints(user.getPoints() + totalPoints);
        userService.updateUser(user);

        return transaction;
    }
    private List<Material> fetchMaterialsByName(List<String> materialNames) {

        return materialRepository.findByNameIn(materialNames);
    }

    private int calculateTotalPoints(List<Material> materials) {
        return materials.stream().mapToInt(Material::getPoints).sum();
    }

    public void addPointsToUser(String username, int points) {
        // Fetch user by username
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            // Update user's total points
            user.get().setPoints(user.get().getPoints() + points);
            userService.updateUser(user.get());
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    public void addPointsToAllUsers(int points) {
        // Get all users
        List<User> users = userService.getAllUsers();

        // Update total points for all users
        users.forEach(user -> {
            user.setPoints(user.getPoints() + points);
            userService.updateUser(user);
        });
    }


    public List<Transaction> getUserTransactionHistory(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return transactionRepository.findByUserOrderByTransactionDateTimeDesc(user);
    }

    public MaterialImpact calculateUserMaterialImpact(String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Transaction> userTransactions = transactionRepository.findByUserOrderByTransactionDateTimeDesc(user);
            return new MaterialImpact(userTransactions);
        } else {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }

    public static class MaterialImpact {
        private final int totalMaterialsRecycled;

        public MaterialImpact(List<Transaction> transactions) {
            // Implement logic to calculate the total number of materials recycled based on transactions
            this.totalMaterialsRecycled = calculateTotalMaterialsRecycled(transactions);
        }

        public int getTotalMaterialsRecycled() {
            return totalMaterialsRecycled;
        }

        private int calculateTotalMaterialsRecycled(List<Transaction> transactions) {
            return transactions.stream()
                    .mapToInt(transaction -> transaction.getMaterials().size())
                    .sum();
        }
    }

}

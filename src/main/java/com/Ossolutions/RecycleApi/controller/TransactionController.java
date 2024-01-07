package com.Ossolutions.RecycleApi.controller;
import com.Ossolutions.RecycleApi.Model.Transaction;
import com.Ossolutions.RecycleApi.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping("/all-transaction")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/make")
    public Transaction makeTransaction(
            @RequestParam String username,
            @RequestParam String recyclingCentername,
            @RequestParam List<String> materials) {
        return transactionService.makeTransaction(username, recyclingCentername, materials);
    }

    @PostMapping("/addPointsToUser")
    public void addPointsToUser(@RequestParam String username, @RequestParam int points) {
        transactionService.addPointsToUser(username, points);
    }

    @PostMapping("/addPointsToAllUsers")
    public void addPointsToAllUsers(@RequestParam int points) {
        transactionService.addPointsToAllUsers(points);
    }
    @GetMapping("/{username}/material-impact")
    public ResponseEntity<UserMaterialImpactResponse> getUserMaterialImpact(@PathVariable String username) {
        try {
            TransactionService.MaterialImpact materialImpact = transactionService.calculateUserMaterialImpact(username);
            return ResponseEntity.ok(new UserMaterialImpactResponse(username, materialImpact));

        } catch (UsernameNotFoundException usernameNotFoundException) {
            throw new UsernameNotFoundException("User not found with username: " + username);

        }
    }

    public class UserMaterialImpactResponse {
        private final String username;
        private final TransactionService.MaterialImpact materialImpact;
        private final String errorMessage;

        public UserMaterialImpactResponse(String username, TransactionService.MaterialImpact materialImpact) {
            this.username = username;
            this.materialImpact = materialImpact;
            this.errorMessage = null;
        }

        public UserMaterialImpactResponse(String errorMessage) {
            this.username = null;
            this.materialImpact = null;
            this.errorMessage = errorMessage;
        }

        public String getUsername() {
            return username;
        }

        public TransactionService.MaterialImpact getMaterialImpact() {
            return materialImpact;
        }

        public String getImpactStatement() {
            if (materialImpact != null) {
                return String.format(
                        "Congratulations, %s! You've contributed to a positive impact on the environment. " +
                                "By recycling %d materials, you've helped reduce carbon emissions and protect the ozone layer. " +
                                "Your efforts truly make a difference!",
                        username, materialImpact.getTotalMaterialsRecycled()
                );
            } else if (errorMessage != null) {
                return errorMessage;
            } else {
                return "User not found. Please check the username and try again.";
            }
        }
    }
}



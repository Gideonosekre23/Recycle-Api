package com.Ossolutions.RecycleApi.Service;

import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Model.Voucher;
import com.Ossolutions.RecycleApi.Repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final UserService userService;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository, UserService userService) {
        this.voucherRepository = voucherRepository;
        this.userService = userService;
    }

    public List<Voucher> getUserVouchers(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return voucherRepository.findByUserOrderByGenerationDateTimeDesc(user);
    }

    public List<Voucher> getNonExpiredUserVouchers(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return voucherRepository.findByUserOrderByGenerationDateTimeDesc(user).stream()
                .filter(voucher -> !isVoucherExpired(voucher.getSerialNumber()))
                .collect(Collectors.toList());
    }

    public Voucher generateVoucher(String username, int pointsUsed, String serialNumber) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Check if the user has enough points
        if (user.getPoints() >= pointsUsed) {
            // Create a new voucher
            Voucher voucher = new Voucher(user, pointsUsed, serialNumber, LocalDateTime.now());
            voucherRepository.save(voucher);

            // Deduct points from the user
            user.setPoints(user.getPoints() - pointsUsed);
            userService.updateUser(user);

            return voucher;
        } else {
            throw new RuntimeException("Not enough points to generate a voucher.");
        }
    }

    public boolean isVoucherExpired(String serialNumber) {
        Voucher voucher = voucherRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("Voucher not found with serial number: " + serialNumber));
        return LocalDateTime.now().isAfter(voucher.getExpirationDateTime());
    }
}

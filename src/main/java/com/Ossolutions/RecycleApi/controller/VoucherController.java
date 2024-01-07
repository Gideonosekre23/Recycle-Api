package com.Ossolutions.RecycleApi.controller;


import com.Ossolutions.RecycleApi.Model.Voucher;
import com.Ossolutions.RecycleApi.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/{username}")
    public List<Voucher> getUserVouchers(@PathVariable String username) {
        return voucherService.getUserVouchers(username);
    }

    @PostMapping("/generate")
    public Voucher generateVoucher(@RequestParam String username, @RequestParam int pointsUsed, @RequestParam String serialNumber) {
        return voucherService.generateVoucher(username, pointsUsed, serialNumber);
    }

    @GetMapping("/isExpired/{serialNumber}")
    public boolean isVoucherExpired(@PathVariable String serialNumber) {
        return voucherService.isVoucherExpired(serialNumber);
    }
}

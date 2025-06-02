package com.forum.controller;

import com.forum.model.admin.dto.PremiumFeeDTO;
import com.forum.service.PremiumFeeService;
import com.forum.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/premium")
public class PremiumFeeController {

    @Autowired
    private PremiumFeeService premiumFeeService;

    @GetMapping("/current-fee")
    public Result<?> getFees(@RequestHeader("Admin-Token") String adminToken) {
        return Result.success(premiumFeeService.getFees());
    }

    @PutMapping("/next-month-fee")
    public Result<?> setNextMonthFee(
            @RequestBody PremiumFeeDTO premiumFeeDTO,
            @RequestHeader("Admin-Token") String adminToken) {
        premiumFeeService.setNextMonthFee(premiumFeeDTO.getAmount());
        return Result.success();
    }

    @PutMapping("/wallet-address")
    public Result<?> updateWalletAddress(
            @RequestBody String newAddress,
            @RequestHeader("Admin-Token") String adminToken) {
        premiumFeeService.updateWalletAddress(newAddress);
        return Result.success();
    }


}

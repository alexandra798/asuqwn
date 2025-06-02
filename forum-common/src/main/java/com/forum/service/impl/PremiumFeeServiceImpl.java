// PremiumFeeServiceImpl.java
package com.forum.service.impl;

import com.forum.mapper.PremiumFeeMapper;
import com.forum.model.entity.PremiumFee;
import com.forum.service.PremiumFeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PremiumFeeServiceImpl implements PremiumFeeService {

    private final PremiumFeeMapper premiumFeeMapper;

    @Value("${premium.wallet.address}")
    private String walletAddress;

    @Override
    public List<PremiumFee> getFees() {
        return premiumFeeMapper.findAll();
    }

    @Override
    @Transactional
    public void setNextMonthFee(BigDecimal amount) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMonthStart = now.plusMonths(1).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextMonthEnd = nextMonthStart.plusMonths(1).minusSeconds(1);

        PremiumFee fee = new PremiumFee();
        fee.setAmount(amount);
        fee.setStartTime(nextMonthStart);
        fee.setEndTime(nextMonthEnd);

        premiumFeeMapper.insert(fee);
        log.info("下月会员费设置成功: amount={}, startTime={}", amount, nextMonthStart);
    }

    @Override
    public void updateWalletAddress(String address, String adminToken) {
        // 这里应该验证管理员token
        this.walletAddress = address;
        // 实际应该保存到配置表中
        log.info("钱包地址更新成功: address={}", address);
    }
}

package com.forum.service;


import com.forum.model.entity.PremiumFee;

import java.math.BigDecimal;
import java.util.List;

public interface PremiumFeeService {

    List<PremiumFee> getFees();
    void setNextMonthFee(BigDecimal amount);
    void updateWalletAddress(String address);

}

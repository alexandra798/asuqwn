package com.forum.model.user.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PremiumFeeVO {
    private BigDecimal amount;
    private boolean isPremium;
}

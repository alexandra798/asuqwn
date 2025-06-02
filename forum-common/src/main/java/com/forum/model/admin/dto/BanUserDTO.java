package com.forum.model.admin.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class BanUserDTO {
    @NotNull(message = "封禁天数不能为空")
    @Min(value = 1, message = "封禁天数必须大于0")
    private Integer days;
}

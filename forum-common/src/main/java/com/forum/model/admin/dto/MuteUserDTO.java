package com.forum.model.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MuteUserDTO {
    @NotNull(message = "禁言天数不能为空")
    @Min(value = 1, message = "禁言天数必须大于0")
    private Integer days;
}

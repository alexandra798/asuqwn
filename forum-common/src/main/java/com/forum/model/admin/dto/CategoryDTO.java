package com.forum.model.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CategoryDTO {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    @NotBlank(message = "发帖准则不能为空")
    private String guidelines;
}

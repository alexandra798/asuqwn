package com.forum.model.user.query;

import lombok.Data;

@Data
public class PostQueryDTO {

    private String type;
    private int pageNo = 1;
    private int pageSize = 40;

}


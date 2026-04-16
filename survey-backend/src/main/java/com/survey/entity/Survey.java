package com.survey.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("survey")
public class Survey {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String coverImage;

    private String status;  // draft, published, closed

    private Long userId;

    private Integer responseCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<Question> questions;
}

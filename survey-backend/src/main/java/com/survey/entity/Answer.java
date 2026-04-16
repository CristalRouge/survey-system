package com.survey.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("answer")
public class Answer {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long responseId;

    private Long questionId;

    private String value;  // JSON格式存储，支持多选

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}

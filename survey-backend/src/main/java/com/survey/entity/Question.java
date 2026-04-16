package com.survey.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long surveyId;

    private String type;  // single, multiple, dropdown, text

    private String title;

    private String description;

    private String image;

    private Boolean required;

    private String placeholder;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<QuestionOption> options;
}
